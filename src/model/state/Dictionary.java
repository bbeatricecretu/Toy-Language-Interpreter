package model.state;

import model.exception.OperationExceptions;
import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class Dictionary<T> implements IDictionary<T> {
    private final Map<String, T> dict;

    public Dictionary() {
        this.dict = new HashMap<>();
    }

    @Override
    public boolean isDefined(String key) {
        return dict.containsKey(key);
    }

    @Override
    public void setValue(String key, T value) {
        if (!isDefined(key)) {
            throw new OperationExceptions(key + " is not defined yet");
        }
        dict.put(key, value);
    }

    @Override
    public T getValue(String key) {
        if (!isDefined(key)) {
            throw new OperationExceptions(key + " not defined");
        }
        return dict.get(key);
    }

    public Type getType(String key) {
        // This helper is for the execution logic which uses Dictionary<Value>
        if (getValue(key) instanceof Value v) {
            return v.getType();
        }
        throw new OperationExceptions("Dictionary does not hold Values");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void declareVar(Type type, String key) {
        if (isDefined(key)) {
            throw new OperationExceptions(key + " is already defined");
        }
        // Used during execution (T is Value)
        dict.put(key, (T) type.getDefaultValue());
    }

    @Override
    public T lookup(String name) {
        return dict.get(name);
    }

    @Override
    public void update(String name, T value) {
        dict.put(name, value);
    }

    @Override
    public void put(String key, T value) {
        dict.put(key, value);
    }

    @Override
    public Map<String, T> getValues() {
        return new HashMap<>(dict);
    }

    @Override
    public IDictionary<T> deepCopy() {
        Dictionary<T> newDict = new Dictionary<>();
        for (Map.Entry<String, T> entry : dict.entrySet()) {
            newDict.put(entry.getKey(), entry.getValue());
        }
        return newDict;
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

    @Override
    public Map<String, T> getContent() {
        return this.dict; // Returns the internal HashMap
    }
}