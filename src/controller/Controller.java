package controller;

import model.exception.StatementException;
import model.state.IHeap;
import model.state.IStack;
import model.state.ProgramState;
import model.statement.Statement;
import repository.IRepository;

import java.util.List;

public class Controller {
    IRepository repo;
    private boolean flag = false;

    public Controller(IRepository repository) {
        this.repo = repository;
    }

    public void setFlag(boolean displayFlag) {
        this.flag = displayFlag;
    }

    public ProgramState executeOneStep(ProgramState state) {
        IStack<Statement> stack = state.executionStack();
        if (stack.isEmpty()) {
            throw new StatementException("ProgramState Stack is Empty");
        }
        Statement statement = stack.pop();
        return statement.execute(state);
    }

    private void callGarbageCollector(ProgramState state) {
        // Extract the referenced heap addresses from symbol table values
        List<Integer> reachableAddresses =
                IHeap.getAddrFromValues(state.symbolTable().getValues().values());

        // Perform safe GC on the heap
        state.heap().safeGarbageCollector(reachableAddresses);
    }

    public void executeAllSteps() {
        ProgramState program = repo.getCrtPrg();

        if (flag) {
            System.out.println("\nINITIAL PROGRAM STATE");
            System.out.println(program.toString());
        }

        repo.logPrgStateExec();

        while (!program.executionStack().isEmpty()) {
            executeOneStep(program);

            // run garbage collector AFTER each step
            callGarbageCollector(program);

            repo.logPrgStateExec();

            if (flag) {
                System.out.println("\nNEXT STEP");
                System.out.println(program.toString());
            }
        }
    }

    public void displayCurrentState() {
        ProgramState program = repo.getCrtPrg();
        System.out.println(program.toString());
    }
}
