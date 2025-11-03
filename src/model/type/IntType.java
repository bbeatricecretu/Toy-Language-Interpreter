package model.type;

import model.value.IntValue;
import model.value.Value;

public class IntType implements Type {

  @Override
  public boolean equals(Object another) {
    return another instanceof IntType;
  }

  @Override
  public int hashCode() {
    return 1; //hash for IntType objects
  }

  @Override
  public String toString() {
    return "int";
  }

  @Override
  public Value getDefaultValue() {
    return new IntValue(0);
  }
}
