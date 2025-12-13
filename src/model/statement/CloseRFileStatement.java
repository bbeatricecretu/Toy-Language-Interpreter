package model.statement;

import model.exception.FileException;
import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.StringType;
import model.type.Type;
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
        Value exprValue = expression.evaluate((Dictionary<Value>) state.symbolTable(), state.heap());
        if (!(exprValue.getType() instanceof StringType)) {
            throw new StatementException("Expression must evaluate to a string.");
        }

        String fileName = ((StringValue) exprValue).getValue();

        if (!state.fileTable().isOpened(fileName)) {
            throw new FileException("File " + fileName + " is not opened.");
        }

        BufferedReader reader = state.fileTable().getFile(fileName);
        try {
            reader.close();
        } catch (IOException e) {
            throw new FileException("Error closing file: " + fileName);
        }

        state.fileTable().delete(fileName);

        return state;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        if (expression.typecheck(typeEnv).equals(new StringType())) {
            return typeEnv;
        } else {
            throw new TypeCheckException("CloseRFile requires a string expression");
        }
    }
}