package model.expression;

import model.exception.ExpressionException;
import model.state.Dictionary;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;

//Performs && or ||
public record LogicalExpression(Expression left, Expression right, String operator)
        implements Expression {

    @Override
    public Value evaluate(Dictionary<Value> symbolTable) {
        Value vLeft = left.evaluate(symbolTable);
        Value vRight = right.evaluate(symbolTable);

        if (!vLeft.getType().equals(new BoolType())) {
            throw new ExpressionException("Left operand is not an Boolean Type");
        }

        if (!vRight.getType().equals(new BoolType())) {
            throw new ExpressionException("Right operand is not an Boolean Type");
        }

        boolean bLeft = ((BoolValue) vLeft).value();
        boolean bRight = ((BoolValue) vRight).value();

        if (operator.equals("&&")) return new BoolValue(bLeft && bRight);
        if (operator.equals("||")) return new BoolValue(bLeft || bRight);
        else throw new ExpressionException("Invalid Operator for Logical Expression" + operator);
    }
}
