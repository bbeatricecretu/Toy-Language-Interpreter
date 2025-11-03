package model.state;

import model.statement.Statement;
import model.value.Value;

public record ProgramState(
    MyStack<Statement> executionStack, MyIDictionary<Value> symbolTable, MyList<Value> out) {

    @Override
    public String toString() {
        return  "Execution Stack:\n" + executionStack +
                "\nSymbol Table:\n" + symbolTable +
                "\nOutput:\n" + out;
    }

}
