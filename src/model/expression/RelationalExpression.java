package model.expression;

import model.exception.ExpressionException;
import model.state.IDictionary;
import model.state.IHeap;
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
    public Value evaluate(IDictionary<Value> symTable, IHeap heap) {

        // Evaluate both expressions with heap support
        Value v1 = exp1.evaluate(symTable, heap);
        Value v2 = exp2.evaluate(symTable, heap);

        // Type checking
        if (!(v1.getType() instanceof IntType)) {
            throw new ExpressionException("First operand is not an integer: " + v1);
        }
        if (!(v2.getType() instanceof IntType)) {
            throw new ExpressionException("Second operand is not an integer: " + v2);
        }

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        // Perform relational operation
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
        return "(" + exp1 + " " + operator + " " + exp2 + ")";
    }
}
