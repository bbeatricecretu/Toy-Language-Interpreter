package model.statement;

import model.exception.StatementException;
import model.expression.Expression;
import model.state.IDictionary;
import model.state.IHeap;
import model.state.IStack;
import model.state.ProgramState;
import model.type.BoolType;
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

        IDictionary<Value> symTable = state.symbolTable();
        IHeap heap = state.heap();
        IStack<Statement> stack = state.executionStack();

        // 1. evaluate the condition
        Value condValue = condition.evaluate(symTable, heap);

        // 2. check if boolean
        if (!condValue.getType().equals(new BoolType())) {
            throw new StatementException("While condition is not boolean");
        }

        BoolValue boolVal = (BoolValue) condValue;

        // 3. if false → skip
        if (!boolVal.value()) {
            return state;
        }

        // 4. if true → push body and then while(...) again
        stack.push(this);   // repeat loop
        stack.push(body);   // execute body first

        return state;
    }

    @Override
    public String toString() {
        return "while(" + condition + ") " + body;
    }
}
