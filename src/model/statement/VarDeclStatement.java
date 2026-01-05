package model.statement;

import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;

public record VarDeclStatement(Type type, String variableName) implements Statement {

    @Override
    public String toString() {
        return type.toString() + " " + variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        if (state.getSymTable().isDefined(variableName)) {
            throw new StatementException("Variable " + variableName + " is already defined");
        }
        state.getSymTable().declareVar(type, variableName);
        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        typeEnv.put(variableName, type); //
        return typeEnv;
    }
}