package model.state;

import model.type.Type;
import model.value.Value;

import java.util.Map;

public interface IDictionary<T> {

    boolean isDefined(String key);

    void setValue(String key, T value);

    T getValue(String key);

    void declareVar(Type type, String key);

    T lookup(String name);

    void update(String name, T value);

    Map<String, T> getValues();

    void put(String key, T value); // Added for TypeChecker

    IDictionary<T> deepCopy(); // Added for TypeChecker
}