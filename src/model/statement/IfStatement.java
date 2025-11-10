package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;

public record IfStatement(Expression condition, Statement trueStatement, Statement falseStatement)
        implements Statement {

    @Override
    public String toString() {
        return "IF (" + condition + "); THEN " + trueStatement + "; ELSE " + falseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate((Dictionary<Value>) state.symbolTable()); //evaluate from Expression

        if (!value.getType().equals(new BoolType())) {
            throw new StatementException("The condition have not a boolean value");
        }

        BoolValue booleanValue = (BoolValue) value;
        if (booleanValue.value()) {
            state.executionStack().push(trueStatement);
        } else {
            state.executionStack().push(falseStatement);
        }
        return state;
    }
}
