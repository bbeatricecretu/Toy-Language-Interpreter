package model.statement;

import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state);

    IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException;
}