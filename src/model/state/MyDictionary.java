package model.state;

import model.exception.OperationExceptions;
import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

//SymbolTable
public class MyDictionary<T extends Value> implements MyIDictionary<T> { //can only store objects that implement the Value interface
  Map<String, T> dict;

  //Constructor
  public MyDictionary() {
    dict = new HashMap<>();
  }

  @Override
  public void setValue(String key, T value) {
    if (!isDefined(key)) {
      throw new OperationExceptions(key + " is not defined yet");
    }
    dict.put(key, value);
  }

  @Override
  public boolean isDefined(String key) {
    return dict.containsKey(key);
  }

  @Override
  public Type getType(String key) {
    return getValue(key).getType();
  }

  // the ADT Dictionary must be a generic one, and I cannot
  // tell the compiler that it should expect only Value objects,we have to cast to supress warning ("unchecked")

  @SuppressWarnings("unchecked") //Hides all unchecked warnings that might appear inside the method.
  @Override
  public void declareVar(Type type, String key) {
    if (isDefined(key)) {
      throw new OperationExceptions(key + " is already defined");
    }
    dict.put(key, (T) type.getDefaultValue());
  }

  @Override
  public T getValue(String key) {
    if (!isDefined(key)) {
      throw new OperationExceptions(key + "not defined");
    }
    return dict.get(key);
  }

    @Override
    public String toString() {
        if (dict.isEmpty()) return "(empty)";

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, T> entry : dict.entrySet()) {
            sb.append("  ").append(entry.getKey())
                    .append(" → ")
                    .append(entry.getValue())
                    .append("\n");
        }
        return sb.toString();
    }

}
