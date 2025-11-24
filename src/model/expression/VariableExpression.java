package model.expression;

import model.exception.ExpressionException;
import model.state.IDictionary;
import model.state.IHeap;
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
    public String toString() {
        return variableName;
    }

}
