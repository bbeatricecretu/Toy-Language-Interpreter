package model.expression;

import model.exception.ExpressionException;
import model.state.MyDictionary;
import model.type.IntType;
import model.value.IntValue;
import model.value.Value;

//Performs the operation (+, -, *, /).
public record ArithmeticExpression(Expression left, Expression right, char operator)
        implements Expression {

    @Override
    public Value evaluate(MyDictionary<Value> symbolTable) {
        Value vLeft = left.evaluate(symbolTable);
        Value vRight = right.evaluate(symbolTable);

        if (!vLeft.getType().equals(new IntType())) {
            throw new ExpressionException("Left operand is not an Integer Type");
        }

        if (!vRight.getType().equals(new IntType())) {
            throw new ExpressionException("Right operand is not an Integer Type");
        }

        //compute the result
        int nLeft = ((IntValue) vLeft).value();
        int nRight = ((IntValue) vRight).value();

        if (operator == '+') return new IntValue(nLeft + nRight);
        if (operator == '-') return new IntValue(nLeft - nRight);
        if (operator == '*') return new IntValue(nLeft * nRight);
        if (operator == '/') {
            if (nRight == 0)
                throw new ExpressionException("Cannot divide by zero");
            return new IntValue(nLeft / nRight);
        }
        else throw new ExpressionException("Invalid operator for Arithmetic Expression: " + operator);
    }
}
