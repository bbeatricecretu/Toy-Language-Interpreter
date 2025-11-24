package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.IDictionary;
import model.state.IHeap;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.Value;

public class HeapAllocation implements Statement {

    private final String varName;
    private final Expression expression;

    public HeapAllocation(String varName, Expression expression) {
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

        // 2. variable must be RefType
        Value varValue = symTable.getValue(varName);

        if (!(varValue.getType() instanceof RefType refType)) {
            throw new StatementException("Variable " + varName + " is not a reference type");
        }

        // 3. evaluate expression
        Value exprValue = expression.evaluate(symTable, heap);

        // 4. type check
        if (!exprValue.getType().equals(refType.getInner())) {
            throw new StatementException("Type mismatch: expected "
                    + refType.getInner() + " but found " + exprValue.getType());
        }

        // 5. allocate
        int newAddress = heap.allocate(exprValue);

        // 6. update symbol table
        RefValue newRef = new RefValue(newAddress, refType.getInner());
        symTable.setValue(varName, newRef);

        return state;
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }
}
