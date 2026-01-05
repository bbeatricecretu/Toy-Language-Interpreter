package model.statement;

import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.IDictionary;
import model.state.IHeap;
import model.state.IStack;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public class WhileStatement implements Statement {

    private final Expression condition;
    private final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IDictionary<Value> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        IStack<Statement> stack = state.getExeStack();

        Value condValue = condition.evaluate(symTable, heap);

        if (!condValue.getType().equals(new BoolType())) {
            throw new StatementException("While condition is not boolean");
        }

        BoolValue boolVal = (BoolValue) condValue;

        if (boolVal.getValue()) {
            stack.push(this);
            stack.push(body);
        }
        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeExpr = condition.typecheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            body.typecheck(typeEnv.deepCopy());
            return typeEnv;
        } else {
            throw new TypeCheckException("The condition of WHILE has not the type bool");
        }
    }

    @Override
    public String toString() {
        return "while(" + condition + ") " + body;
    }
}