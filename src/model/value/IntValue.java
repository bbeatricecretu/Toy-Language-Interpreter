package model.value;

import model.type.IntType;
import model.type.Type;

public record IntValue(int value) implements Value {
    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public String toString() {
        return String.valueOf(value); //Converts the stored integer into a string
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof IntValue)) return false;
        IntValue other = (IntValue) another;
        return this.value == other.value;
    }
}
