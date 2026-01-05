package model.statement;

import model.exception.FileException;
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
import java.io.FileReader;
import java.io.IOException;

public record OpenRFileStatement(Expression expression) implements Statement {

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value value = expression.evaluate((Dictionary<Value>) state.getSymTable(), state.getHeap());
        if (!(value.getType() instanceof StringType)) {
            throw new FileException("Expression must evaluate to a string.");
        }

        String fileName = ((StringValue) value).getValue();

        if (state.getFileTable().isOpened(fileName)) {
            throw new FileException("File already opened: " + fileName);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            state.getFileTable().add(fileName, reader);
        } catch (IOException e) {
            throw new FileException("Could not open file: " + fileName);
        }

        return null;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeExpr.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new TypeCheckException("OpenRFile requires a string expression");
        }
    }
}