package model.expression;

import model.state.Dictionary;
import model.value.Value;

public interface Expression {
    Value evaluate(Dictionary<Value> symbolTable);
}
