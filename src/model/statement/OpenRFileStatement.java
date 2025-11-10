package model.statement;

import model.exception.FileException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record OpenRFileStatement(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // evaluate expression and ensure it’s a string
        Value value = expression.evaluate((Dictionary<Value>) state.symbolTable());
        if (!(value.getType() instanceof StringType)) {
            throw new FileException("Expression must evaluate to a string.");
        }

        String fileName = ((StringValue) value).getValue();

        // check if already opened
        if (state.fileTable().isOpened(fileName)) {
            throw new FileException("File already opened: " + fileName);
        }

        // open and add to file table
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            state.fileTable().add(fileName, reader);
        } catch (IOException e) {
            throw new FileException("Could not open file: " + fileName);
        }

        return state;
    }
}
