package model.value;

import model.type.RefType;
import model.type.Type;

public record RefValue(int address, Type locationType) implements Value {

    public int getAddr() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }

    @Override
    public boolean equals(Object another) {
        if (!(another instanceof RefValue other)) return false;
        return this.address == other.address &&
                this.locationType.equals(other.locationType);
    }
}
