package model.statement;

import model.expression.Expression;
import model.state.Dictionary;
import model.state.ProgramState;
import model.value.Value;

public record PrintStatement(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "print (" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate((Dictionary<Value>) state.symbolTable());
        state.out().add(value);
        return state;
    }
}
