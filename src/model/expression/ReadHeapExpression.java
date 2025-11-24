package model.expression;

import model.exception.ExpressionException;
import model.state.IDictionary;
import model.state.IHeap;
import model.value.RefValue;
import model.value.Value;

public class ReadHeapExpression implements Expression {

    private final Expression expression;

    public ReadHeapExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value evaluate(IDictionary<Value> symbolTable, IHeap heap) {

        // 1. Evaluate inner expression
        Value value = expression.evaluate(symbolTable, heap);

        // 2. Must be a RefValue
        if (!(value instanceof RefValue refValue)) {
            throw new ExpressionException("rH argument must be a reference value");
        }

        int address = refValue.getAddr();

        // 3. Check address exists
        if (!heap.isAllocated(address)) {
            throw new ExpressionException("Invalid heap address: " + address);
        }

        // 4. Read from heap
        return heap.getValue(address);
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}
