package model.expression;

import model.exception.ExpressionException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.IntType;
import model.value.IntValue;
import model.value.Value;

public record ArithmeticExpression(Expression left, Expression right, char operator)
        implements Expression {

    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {

        Value vLeft = left.evaluate(symbolTable, heap);
        Value vRight = right.evaluate(symbolTable, heap);

        if (!vLeft.getType().equals(new IntType())) {
            throw new ExpressionException("Left operand is not an Integer Type");
        }

        if (!vRight.getType().equals(new IntType())) {
            throw new ExpressionException("Right operand is not an Integer Type");
        }

        int nLeft = ((IntValue) vLeft).value();
        int nRight = ((IntValue) vRight).value();

        return switch (operator) {
            case '+' -> new IntValue(nLeft + nRight);
            case '-' -> new IntValue(nLeft - nRight);
            case '*' -> new IntValue(nLeft * nRight);
            case '/' -> {
                if (nRight == 0)
                    throw new ExpressionException("Cannot divide by zero");
                yield new IntValue(nLeft / nRight);
            }
            default -> throw new ExpressionException("Invalid operator: " + operator);
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}
