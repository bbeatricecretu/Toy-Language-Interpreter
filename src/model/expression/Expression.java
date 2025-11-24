package model.expression;

import model.state.IDictionary;
import model.state.IHeap;
import model.value.Value;

public interface Expression {
    Value evaluate(IDictionary<Value> symbolTable, IHeap heap);
}
