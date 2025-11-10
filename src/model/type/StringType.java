package model.type;

import model.value.StringValue;
import model.value.Value;

public class StringType implements Type {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    @Override
    public int hashCode() {
        return 3; //hash for IntType objects
    }

    @Override
    public String toString() {
        return "String";
    }

    @Override
    public Value getDefaultValue() {
        return new StringValue("");
    }
}
