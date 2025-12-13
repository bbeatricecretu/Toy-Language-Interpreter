package model.expression;

import model.exception.ExpressionException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public class RelationalExpression implements Expression {
    private final Expression exp1;
    private final Expression exp2;
    private final String operator;

    public RelationalExpression(Expression exp1, Expression exp2, String operator) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operator = operator;
    }

    @Override
    public Value evaluate(IDictionary<Value> symTable, IHeap heap) {
        Value v1 = exp1.evaluate(symTable, heap);
        Value v2 = exp2.evaluate(symTable, heap);

        if (!(v1.getType() instanceof IntType)) throw new ExpressionException("First operand is not an integer");
        if (!(v2.getType() instanceof IntType)) throw new ExpressionException("Second operand is not an integer");

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        return switch (operator) {
            case "<" -> new BoolValue(n1 < n2);
            case "<=" -> new BoolValue(n1 <= n2);
            case "==" -> new BoolValue(n1 == n2);
            case "!=" -> new BoolValue(n1 != n2);
            case ">" -> new BoolValue(n1 > n2);
            case ">=" -> new BoolValue(n1 >= n2);
            default -> throw new ExpressionException("Invalid relational operator: " + operator);
        };
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typ1 = exp1.typecheck(typeEnv);
        Type typ2 = exp2.typecheck(typeEnv);

        if (!typ1.equals(new IntType())) throw new TypeCheckException("Relational: first operand is not an integer");
        if (!typ2.equals(new IntType())) throw new TypeCheckException("Relational: second operand is not an integer");
        return new BoolType();
    }

    @Override
    public String toString() {
        return "(" + exp1 + " " + operator + " " + exp2 + ")";
    }
}