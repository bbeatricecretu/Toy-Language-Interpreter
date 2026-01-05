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

public class HeapAllocation implements Statement {

    private final String varName;
    private final Expression expression;

    public HeapAllocation(String varName, Expression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IDictionary<Value> symTable = state.getSymTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("Variable " + varName + " is not defined.");
        }
        Value varValue = symTable.getValue(varName);
        if (!(varValue.getType() instanceof RefType refType)) {
            throw new StatementException("Variable " + varName + " is not a reference type");
        }

        Value exprValue = expression.evaluate(symTable, heap);
        if (!exprValue.getType().equals(refType.getInner())) {
            throw new StatementException("Type mismatch: expected "
                    + refType.getInner() + " but found " + exprValue.getType());
        }

        int newAddress = heap.allocate(exprValue);
        symTable.setValue(varName, new RefValue(newAddress, refType.getInner()));

        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeVar = typeEnv.lookup(varName);
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr))) {
            return typeEnv;
        } else {
            throw new TypeCheckException("NEW stmt: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }
}