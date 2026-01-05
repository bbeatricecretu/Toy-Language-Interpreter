package model.statement;

import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;

public record CompStatement(Statement first, Statement second) implements Statement {

    @Override
    public String toString() {
        return "(" + first.toString() + "; " + second.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        return second.typecheck(first.typecheck(typeEnv)); //
    }
}