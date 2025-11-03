package model.statement;

import model.exception.StatementException;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;

public record VarDeclStatement(Type type, String variableName) implements Statement {

  @Override
  public String toString() {
    if (type.equals(new IntType())) {
      return "Int " + variableName;
    }
    if (type.equals(new BoolType())) {
      return "Bool " + variableName;
    }
    return "Var " + variableName; //default type
  }

  @Override
  public ProgramState execute(ProgramState state) {
    if (state.symbolTable().isDefined(variableName)) {
      throw new StatementException("Variable " + variableName + " is already defined");
    }
    state.symbolTable().declareVar(type, variableName);
    return state;
  }
}
