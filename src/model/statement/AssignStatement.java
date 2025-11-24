package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.ProgramState;
import model.value.Value;

public record AssignStatement(String variableName, Expression expression) implements Statement {

    @Override
    public String toString() {
        return variableName + " = " + expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Dictionary<Value> symbolTable = (Dictionary<Value>) state.symbolTable();
        if (!symbolTable.isDefined(variableName)) {
            throw new StatementException("Variable " + variableName + " is not defined");
        }

        var value = expression.evaluate(symbolTable, state.heap());
        var variableType = symbolTable.getType(variableName);

        if (!variableType.equals(value.getType())) {
            throw new StatementException(
                    "Variable " + variableName + " is not assignable to " + variableType);
        }

        symbolTable.setValue(variableName, value);

        return state;
    }
}
