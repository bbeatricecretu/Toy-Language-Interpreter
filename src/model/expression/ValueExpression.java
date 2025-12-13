package model.expression;

import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.Type;
import model.value.Value;

public record ValueExpression(Value value) implements Expression {
    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {
        return value;
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}