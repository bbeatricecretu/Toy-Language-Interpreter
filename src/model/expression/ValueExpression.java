package model.expression;

import model.state.IDictionary;
import model.state.IHeap;
import model.value.Value;

// It simply returns the wrapped constant value.
public record ValueExpression(Value value) implements Expression {

    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {
        return value; // Constants do not depend on symbol table or heap
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
