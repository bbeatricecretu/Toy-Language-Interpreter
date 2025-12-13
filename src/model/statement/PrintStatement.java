package model.statement;

import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public record PrintStatement(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate((Dictionary<Value>) state.symbolTable(), state.heap());
        state.out().add(value);
        return state;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        expression.typecheck(typeEnv);
        return typeEnv; //
    }
}