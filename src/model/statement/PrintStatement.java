package model.statement;

import model.expression.Expression;
import model.state.MyDictionary;
import model.state.ProgramState;
import model.value.Value;

public record PrintStatement(Expression expression) implements Statement {

  @Override
  public String toString() {
    return "print (" + expression.toString() + ")";
  }

  @Override
  public ProgramState execute(ProgramState state) {
    Value value = expression.evaluate((MyDictionary<Value>) state.symbolTable());
    state.out().add(value);
    return state;
  }
}
