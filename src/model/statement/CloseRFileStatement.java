package model.statement;

import model.exception.FileException;
import model.exception.StatementException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStatement(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "closeRFile(" + expression + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // Evaluate expression
        Value exprValue = expression.evaluate((Dictionary<Value>) state.symbolTable(), state.heap());
        if (!(exprValue.getType() instanceof StringType)) {
            throw new StatementException("Expression must evaluate to a string.");
        }

        String fileName = ((StringValue) exprValue).value();

        // Check if file is opened
        if (!state.fileTable().isOpened(fileName)) {
            throw new FileException("File " + fileName + " is not opened.");
        }

        // Get reader and close it
        BufferedReader reader = state.fileTable().getFile(fileName);
        try {
            reader.close();
        } catch (IOException e) {
            throw new FileException("Error closing file: " + fileName);
        }

        // Remove from file table
        state.fileTable().delete(fileName);

        return state;
    }
}
