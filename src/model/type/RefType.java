package model.type;

import model.value.RefValue;
import model.value.Value;

public class RefType implements Type {

    private final Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType) {
            return inner.equals(((RefType) another).inner);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return inner.hashCode() * 31; //standard prime number for stable, low-collision hash codes.
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public Value getDefaultValue() {
        return new RefValue(0, inner);
    }
}
