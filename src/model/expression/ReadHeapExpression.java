package model.expression;

import model.exception.ExpressionException;
import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;

public class ReadHeapExpression implements Expression {
    private final Expression expression;

    public ReadHeapExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {
        Value value = expression.evaluate(symbolTable, heap);
        if (!(value instanceof RefValue refValue)) {
            throw new ExpressionException("rH argument must be a reference value");
        }
        int address = refValue.getAddr();
        if (!heap.isAllocated(address)) {
            throw new ExpressionException("Invalid heap address: " + address);
        }
        return heap.getValue(address);
    }

    @Override
    public Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType refType) {
            return refType.getInner();
        } else {
            throw new TypeCheckException("The rH argument is not a Ref Type");
        }
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}