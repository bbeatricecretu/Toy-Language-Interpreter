package controller;

import model.state.IHeap;
import model.state.ProgramState;
import repository.IRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;
    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repo = repository;
    }

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {
        // Log states before execution
        prgList.forEach(repo::logPrgStateExec);

        // Prepare the list of callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        // Execute concurrently and collect forked states
        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // Add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);

        // Log states after execution
        prgList.forEach(repo::logPrgStateExec);

        // Update the repository state
        repo.setPrgList(prgList);
    }

    public void executeAllSteps() {
        executor = Executors.newFixedThreadPool(2); //

        // Initial list of programs
        List<ProgramState> prgList = removeCompletedPrg(repo.getPrgList());

        while (!prgList.isEmpty()) {
            // Garbage Collector takes into account all shared symbol tables
            conservativeGarbageCollector(prgList);

            try {
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }

            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();

        // Update the final repository state
        repo.setPrgList(prgList);
    }

    private void conservativeGarbageCollector(List<ProgramState> prgList) {
        // Collect addresses from all active symbol tables
        List<Integer> allAddresses = prgList.stream()
                .flatMap(p -> IHeap.getAddrFromValues(p.getSymTable().getValues().values()).stream())
                .collect(Collectors.toList());

        // Shared Heap update
        if (!prgList.isEmpty()) {
            prgList.get(0).getHeap().safeGarbageCollector(allAddresses);
        }
    }
}