package model.state;

import model.statement.Statement;
import model.value.Value;

public record ProgramState(
        Stack<Statement> executionStack, IDictionary<Value> symbolTable, List<Value> out, IFileTable fileTable) {

    @Override
    public String toString() {
        return "Execution Stack:\n" + executionStack +
                "\nSymbol Table:\n" + symbolTable +
                "\nOutput:\n" + out +
                "\nFile Table:\n" + fileTable.toString();
    }

}
