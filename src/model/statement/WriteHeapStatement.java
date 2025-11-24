package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.IDictionary;
import model.state.IHeap;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.Value;

public class WriteHeapStatement implements Statement {

    private final String varName;
    private final Expression expression;

    public WriteHeapStatement(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {

        IDictionary<Value> symTable = state.symbolTable();
        IHeap heap = state.heap();

        // 1. variable must exist
        if (!symTable.isDefined(varName)) {
            throw new StatementException("Variable " + varName + " is not defined.");
        }

        // 2. variable must hold a RefValue
        Value varValue = symTable.getValue(varName);
        if (!(varValue instanceof RefValue refVal)) {
            throw new StatementException("Variable " + varName + " is not a reference type.");
        }

        int address = refVal.getAddr();

        // 3. address must exist in heap
        if (!heap.isAllocated(address)) {
            throw new StatementException("Heap address " + address + " is not allocated.");
        }

        // 4. evaluate the expression
        Value exprValue = expression.evaluate(symTable, heap);

        // 5. type check: expr value must match ref inner type
        RefType refType = (RefType) varValue.getType();
        if (!exprValue.getType().equals(refType.getInner())) {
            throw new StatementException("Type mismatch in write heap: expected "
                    + refType.getInner() + ", got " + exprValue.getType());
        }

        // 6. write into heap
        heap.update(address, exprValue);

        return state;
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression + ")";
    }
}
