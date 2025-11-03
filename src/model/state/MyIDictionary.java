package model.state;

import model.type.Type;

//SymbolTable - handles declaring, updating, and reading variables as the interpreter runs.
public interface MyIDictionary<T> {
  void setValue(String key, T value);

  boolean isDefined(String key);

  Type getType(String key);

  void declareVar(Type type, String key);

  T getValue(String key);

  String toString();
}
