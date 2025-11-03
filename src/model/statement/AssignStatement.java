package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.MyDictionary;
import model.state.ProgramState;
import model.value.Value;

public record AssignStatement(String variableName, Expression expression) implements Statement {

  @Override
  public String toString() {
    return variableName + " = " + expression;
  }

  @Override
  public ProgramState execute(ProgramState state) {
    MyDictionary<Value> symbolTable = (MyDictionary<Value>) state.symbolTable();
    if (!symbolTable.isDefined(variableName)) {
      throw new StatementException("Variable " + variableName + " is not defined");
    }

    var value = expression.evaluate(symbolTable);
    var variableType = symbolTable.getType(variableName);

    if (!variableType.equals(value.getType())) {
      throw new StatementException(
          "Variable " + variableName + " is not assignable to " + variableType);
    }

    symbolTable.setValue(variableName, value);

    return state;
  }
}
