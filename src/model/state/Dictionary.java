package model.state;

import model.exception.OperationExceptions;
import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

// Symbol Table (Dictionary)
public class Dictionary<T extends Value> implements IDictionary<T> {
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

    @Override
    public Type getType(String key) {
        return getValue(key).getType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void declareVar(Type type, String key) {
        if (isDefined(key)) {
            throw new OperationExceptions(key + " is already defined");
        }
        dict.put(key, (T) type.getDefaultValue());
    }

    // Add these two standard methods (required by later labs)
    @Override
    public T lookup(String name) {
        if (!dict.containsKey(name)) {
            throw new OperationExceptions(name + " not defined");
        }
        return dict.get(name);
    }

    @Override
    public void update(String name, T value) {
        dict.put(name, value);
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
    public Map<String, T> getValues() {
        return new HashMap<>(dict);
    }
}
