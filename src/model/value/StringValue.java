package model.value;

import model.type.StringType;
import model.type.Type;

public record StringValue(String value) implements Value {
    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof StringValue)) return false;
        StringValue other = (StringValue) another;
        return this.value.equals(other.value);
    }
}
