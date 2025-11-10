package model.expression;

import model.exception.ExpressionException;
import model.state.Dictionary;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.Value;

public class RelationalExpression implements Expression {
    private final Expression exp1;
    private final Expression exp2;
    private final String operator; // <, <=, ==, !=, >, >=

    public RelationalExpression(Expression exp1, Expression exp2, String operator) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.operator = operator;
    }

    @Override
    public Value evaluate(Dictionary<Value> symTable) {
        Value v1 = exp1.evaluate(symTable);
        if (!(v1.getType() instanceof IntType)) {
            throw new ExpressionException("First operand is not an integer: " + v1);
        }

        Value v2 = exp2.evaluate(symTable);
        if (!(v2.getType() instanceof IntType)) {
            throw new ExpressionException("Second operand is not an integer: " + v2);
        }

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
    public String toString() {
        return "(" + exp1.toString() + " " + operator + " " + exp2.toString() + ")";
    }

}
