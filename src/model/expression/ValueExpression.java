package model.expression;

import model.state.MyDictionary;
import model.value.Value;

//It just returns the stored value (like a constant) - never changes
public record ValueExpression(Value value) implements Expression {

  @Override
  public Value evaluate(MyDictionary<Value> symbolTable) {
    return value;
  }
}
