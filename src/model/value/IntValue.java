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
}
