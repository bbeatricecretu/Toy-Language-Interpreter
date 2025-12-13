package model.expression;

import model.exception.ExpressionException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public record LogicalExpression(Expression left, Expression right, String operator) implements Expression {
    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {
        Value vLeft = left.evaluate(symbolTable, heap);
        Value vRight = right.evaluate(symbolTable, heap);

        if (!vLeft.getType().equals(new BoolType())) throw new ExpressionException("Left operand is not boolean");
        if (!vRight.getType().equals(new BoolType())) throw new ExpressionException("Right operand is not boolean");

        boolean bLeft = ((BoolValue) vLeft).getValue();
        boolean bRight = ((BoolValue) vRight).getValue();

        return switch (operator) {
            case "&&" -> new BoolValue(bLeft && bRight);
            case "||" -> new BoolValue(bLeft || bRight);
            default -> throw new ExpressionException("Invalid logical operator: " + operator);
        };
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typ1 = left.typecheck(typeEnv);
        Type typ2 = right.typecheck(typeEnv);

        if (!typ1.equals(new BoolType())) throw new TypeCheckException("Logical: first operand is not boolean");
        if (!typ2.equals(new BoolType())) throw new TypeCheckException("Logical: second operand is not boolean");
        return new BoolType();
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }
}