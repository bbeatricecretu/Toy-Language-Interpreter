package model.expression;

import model.exception.TypeCheckException;
import model.state.IDictionary;
import model.state.IHeap;
import model.type.Type;
import model.value.Value;

public interface Expression {
    Value evaluate(IDictionary<Value> symbolTable, IHeap heap);

    Type typecheck(IDictionary<Type> typeEnv) throws TypeCheckException;
}