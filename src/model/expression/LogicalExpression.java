package model.expression;

import model.exception.ExpressionException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.Value;

public record LogicalExpression(Expression left, Expression right, String operator)
        implements Expression {

    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {

        // Evaluate left and right expressions with heap support
        Value vLeft = left.evaluate(symbolTable, heap);
        Value vRight = right.evaluate(symbolTable, heap);

        // Type checking
        if (!vLeft.getType().equals(new BoolType())) {
            throw new ExpressionException("Left operand is not of Boolean type");
        }
        if (!vRight.getType().equals(new BoolType())) {
            throw new ExpressionException("Right operand is not of Boolean type");
        }

        boolean bLeft = ((BoolValue) vLeft).value();
        boolean bRight = ((BoolValue) vRight).value();

        // Operators && and ||
        return switch (operator) {
            case "&&" -> new BoolValue(bLeft && bRight);
            case "||" -> new BoolValue(bLeft || bRight);
            default -> throw new ExpressionException("Invalid logical operator: " + operator);
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}
