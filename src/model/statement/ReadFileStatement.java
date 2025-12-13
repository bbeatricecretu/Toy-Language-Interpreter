package model.statement;

import model.exception.FileException;
import model.exception.StatementException;
import model.exception.TypeCheckException;
import model.expression.Expression;
import model.state.Dictionary;
import model.state.IDictionary;
import model.state.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.type.Type;
import model.value.IntValue;
import model.value.StringValue;
import model.value.Value;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStatement(Expression expression, String varName) implements Statement {

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + varName + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) {
        var symTable = (Dictionary<Value>) state.symbolTable();

        if (!symTable.isDefined(varName)) {
            throw new StatementException("Variable " + varName + " is not defined.");
        }
        Value varValue = symTable.lookup(varName);
        if (!(varValue.getType() instanceof IntType)) {
            throw new StatementException("Variable " + varName + " must be of type int.");
        }

        Value exprValue = expression.evaluate((Dictionary<Value>) state.symbolTable(), state.heap());
        if (!(exprValue.getType() instanceof StringType)) {
            throw new FileException("Expression must evaluate to a string.");
        }

        String fileName = ((StringValue) exprValue).getValue();

        if (!state.fileTable().isOpened(fileName)) {
            throw new FileException("File " + fileName + " is not opened.");
        }

        BufferedReader reader = state.fileTable().getFile(fileName);

        try {
            String val = reader.readLine();
            int result;
            if (val == null) {
                result = 0;
            } else {
                result = Integer.parseInt(val.trim());
            }

            symTable.update(varName, new IntValue(result));

        } catch (IOException exception) {
            throw new FileException("Error reading from file: " + fileName);
        } catch (NumberFormatException e) {
            throw new FileException("File " + fileName + " contains non-integer data.");
        }

        return state;
    }

    @Override
    public IDictionary<Type> typecheck(IDictionary<Type> typeEnv) throws TypeCheckException {
        Type typeExpr = expression.typecheck(typeEnv);
        Type typeVar = typeEnv.lookup(varName);
        if (!typeExpr.equals(new StringType())) {
            throw new TypeCheckException("ReadFile requires a string expression");
        }
        if (!typeVar.equals(new IntType())) {
            throw new TypeCheckException("ReadFile requires an int variable");
        }
        return typeEnv;
    }
}