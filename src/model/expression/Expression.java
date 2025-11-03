package model.expression;

import model.state.MyDictionary;
import model.value.Value;

public interface Expression {
  Value evaluate(MyDictionary<Value> symbolTable);
}
