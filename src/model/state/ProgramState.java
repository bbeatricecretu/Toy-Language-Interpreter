package model.state;

import model.statement.Statement;
import model.value.Value;

public class ProgramState {
    private IStack<Statement> executionStack;
    private IDictionary<Value> symbolTable;
    private IList<Value> out;
    private IFileTable fileTable;
    private IHeap heap;
    private int id;
    private static int lastId = 0;

    public ProgramState(IStack<Statement> stack, IDictionary<Value> symTable, IList<Value> out, IFileTable fileTable, IHeap heap) {
        this.executionStack = stack;
        this.symbolTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = getNextId();
    }

    public synchronized static int getNextId() {
        lastId++;
        return lastId;
    }

    // GETTERS to fix your "private access" errors
    public IStack<Statement> getExeStack() {
        return executionStack;
    }

    public IDictionary<Value> getSymTable() {
        return symbolTable;
    }

    public IList<Value> getOut() {
        return out;
    }

    public IFileTable getFileTable() {
        return fileTable;
    }

    public IHeap getHeap() {
        return heap;
    }

    public int getId() {
        return id;
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() {
        if (executionStack.isEmpty()) throw new RuntimeException("Stack is empty");
        Statement crtStmt = executionStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Id: " + id + "\nExeStack:\n" + executionStack + "\nSymTable:\n" + symbolTable +
                "\nOut:\n" + out + "\nFileTable:\n" + fileTable + "\nHeap:\n" + heap + "\n";
    }
}