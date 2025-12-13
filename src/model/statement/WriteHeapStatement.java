package model.statement;

import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.IDictionary;
import model.state.IHeap;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
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

        if (!symTable.isDefined(varName)) {
            throw new StatementException("Variable " + varName + " is not defined.");
        }

        Value varValue = symTable.getValue(varName);
        if (!(varValue instanceof RefValue refVal)) {
            throw new StatementException("Variable " + varName + " is not a reference type.");
        }

        int address = refVal.getAddr();
        if (!heap.isAllocated(address)) {
            throw new StatementException("Heap address " + address + " is not allocated.");
        }

        Value exprValue = expression.evaluate(symTable, heap);
        RefType refType = (RefType) varValue.getType();
        if (!exprValue.getType().equals(refType.getInner())) {
            throw new StatementException("Type mismatch in write heap");
        }

        heap.update(address, exprValue);
        return state;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeVar = typeEnv.lookup(varName);
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr))) {
            return typeEnv;
        } else {
            throw new TypeCheckException("WriteHeap: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression + ")";
    }
}