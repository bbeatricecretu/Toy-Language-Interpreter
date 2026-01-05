package model.statement;

import model.state.ProgramState;
import model.state.Stack;
import model.state.IDictionary;
import model.type.Type;
import model.exception.TypeCheckException;
import model.state.IStack;

public class ForkStatement implements Statement {
    private final Statement statement;

    public ForkStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // 1. Create a new stack for the new thread
        IStack<Statement> newStack = new Stack<>();

        // 2. IMPORTANT: Push the statement to the new stack so it actually runs
        newStack.push(this.statement);

        // 3. Create and return the new thread with shared resources and a cloned SymTable
        return new ProgramState(
                newStack,
                state.getSymTable().deepCopy(),
                state.getOut(),
                state.getFileTable(),
                state.getHeap()
        );
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}