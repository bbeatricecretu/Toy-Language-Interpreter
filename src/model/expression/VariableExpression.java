package model.expression;

import model.exception.ExpressionException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.Type;
import model.value.Value;

public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {
        if (!symbolTable.isDefined(variableName)) {
            throw new ExpressionException("Variable " + variableName + " is not defined");
        }
        return symbolTable.getValue(variableName);
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        return typeEnv.lookup(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}