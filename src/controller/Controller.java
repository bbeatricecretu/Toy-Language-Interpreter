package controller;

import model.exception.StatementException;
import model.state.IStack;
import model.state.ProgramState;
import model.statement.Statement;
import repository.IRepository;

public class Controller {
    IRepository repo;
    private boolean flag = false; //print the internal program state at each step or not.

    //Constructor
    public Controller(IRepository repository) {
        this.repo = repository;
    }

    //Set Display at each step
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

    public void executeAllSteps() {
        ProgramState program = repo.getCrtPrg();

        if (flag) {
            System.out.println("\nINITIAL PROGRAM STATE");
            System.out.println(program.toString());
        }

        repo.logPrgStateExec();

        while (!program.executionStack().isEmpty()) {
            executeOneStep(program);
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
