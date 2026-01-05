package model.statement;

import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public record AssignStatement(String variableName, Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        Dictionary<Value> symbolTable = (Dictionary<Value>) state.getSymTable();
        if (!symbolTable.isDefined(variableName))
            throw new StatementException("Variable " + variableName + " is not defined");
        var value = expression.evaluate(symbolTable, state.getHeap());
        var variableType = symbolTable.getType(variableName);

        if (!variableType.equals(value.getType())) {
            throw new StatementException("Variable " + variableName + " is not assignable to " + variableType);
        }

        symbolTable.setValue(variableName, value);
        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeVar = typeEnv.lookup(variableName);
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeVar.equals(typeExpr)) {
            return typeEnv;
        } else {
            throw new TypeCheckException("Assignment: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return variableName + " = " + expression;
    }
}