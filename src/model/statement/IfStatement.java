package model.statement;

import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public record IfStatement(Expression condition, Statement trueStatement,
                          Statement falseStatement) implements Statement {

    @Override
    public String toString() {
        return "IF (" + condition + ") THEN " + trueStatement + " ELSE " + falseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = condition.evaluate((Dictionary<Value>) state.getSymTable(), state.getHeap());

        if (!value.getType().equals(new BoolType())) {
            throw new StatementException("The condition is not a boolean value");
        }

        BoolValue booleanValue = (BoolValue) value;
        if (booleanValue.getValue()) {
            state.getExeStack().push(trueStatement);
        } else {
            state.getExeStack().push(falseStatement);
        }
        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeExpr = condition.typecheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            trueStatement.typecheck(typeEnv.deepCopy());
            falseStatement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new TypeCheckException("The condition of IF has not the type bool");
        }
    }
}