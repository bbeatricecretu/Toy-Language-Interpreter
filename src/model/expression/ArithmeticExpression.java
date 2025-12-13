package model.expression;

import model.exception.ExpressionException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public record ArithmeticExpression(Expression left, Expression right, char operator) implements Expression {
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

        int nLeft = ((IntValue) vLeft).getValue();
        int nRight = ((IntValue) vRight).getValue();

        return switch (operator) {
            case '+' -> new IntValue(nLeft + nRight);
            case '-' -> new IntValue(nLeft - nRight);
            case '*' -> new IntValue(nLeft * nRight);
            case '/' -> {
                if (nRight == 0) throw new ExpressionException("Cannot divide by zero");
                yield new IntValue(nLeft / nRight);
            }
            default -> throw new ExpressionException("Invalid operator: " + operator);
        };
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typ1 = left.typecheck(typeEnv);
        Type typ2 = right.typecheck(typeEnv);

        if (!typ1.equals(new IntType())) {
            throw new TypeCheckException("Arithmetic: first operand is not an integer");
        }
        if (!typ2.equals(new IntType())) {
            throw new TypeCheckException("Arithmetic: second operand is not an integer");
        }
        return new IntType();
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}