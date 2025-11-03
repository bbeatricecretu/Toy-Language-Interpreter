package model.value;

import model.type.BoolType;
import model.type.Type;

public record BoolValue(boolean value) implements Value {
  @Override
  public Type getType() {
    return new BoolType();
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
