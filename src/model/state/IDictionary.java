package model.state;

import model.type.Type;
import model.value.Value;

import java.util.Map;

public interface IDictionary<T extends Value> {

    boolean isDefined(String key);

    void setValue(String key, T value);

    T getValue(String key);

    Type getType(String key);

    void declareVar(Type type, String key);

    T lookup(String name);

    void update(String name, T value);

    Map<String, T> getValues();
}
