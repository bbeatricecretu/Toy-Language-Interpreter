package model.expression;

import model.exception.ExpressionException;
import model.state.Dictionary;
import model.value.Value;

//When evaluated, it fetches the variable’s value from the current symbol table.
public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(Dictionary<Value> symbolTable) {
        if (!symbolTable.isDefined(variableName)) {
            throw new ExpressionException("Variable " + variableName + " is not defined");
        }
        return symbolTable.getValue(variableName);
    }
}
