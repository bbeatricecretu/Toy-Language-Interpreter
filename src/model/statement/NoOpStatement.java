package model.statement;

import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;

public class NoOpStatement implements Statement {

    @Override
    public String toString() {
        return "NoOperationStatement";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        return state;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        return typeEnv;
    }
}