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

    // ADDED: Getter so the GUI can see the data to update tables
    public IRepository getRepo() {
        return repo;
    }

    // MODIFIED: Added these to let the GUI start/stop the thread pool manually
    public void setExecutor() {
        executor = Executors.newFixedThreadPool(2);
    }

    public void stopExecutor() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    // CHANGED TO PUBLIC: So the GUI button can call it once per click
    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    // CHANGED TO PUBLIC: So the GUI button can call it
    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {
        prgList.forEach(repo::logPrgStateExec);

        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);
        prgList.forEach(repo::logPrgStateExec);
        repo.setPrgList(prgList);
    }

    // KEEP THIS: This is for your old text-based menu (Interpreter.java)
    public void executeAllSteps() {
        setExecutor(); // Use the new helper
        List<ProgramState> prgList = removeCompletedPrg(repo.getPrgList());

        while (!prgList.isEmpty()) {
            conservativeGarbageCollector(prgList);
            try {
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        stopExecutor(); // Use the new helper
        repo.setPrgList(prgList);
    }

    public void conservativeGarbageCollector(List<ProgramState> prgList) {
        List<Integer> allAddresses = prgList.stream()
                .flatMap(p -> IHeap.getAddrFromValues(p.getSymTable().getValues().values()).stream())
                .collect(Collectors.toList());

        if (!prgList.isEmpty()) {
            prgList.get(0).getHeap().safeGarbageCollector(allAddresses);
        }
    }
}