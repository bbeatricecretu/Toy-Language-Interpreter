package model.type;

import model.value.BoolValue;
import model.value.Value;

public class BoolType implements Type {

  @Override
  public boolean equals(Object another) {
    return another instanceof BoolType;
  }

  @Override
  public int hashCode() {
    return 2; //hash for BoolType objects
  }

  @Override
  public String toString() {
    return "bool";
  }

  @Override
  public Value getDefaultValue() {
    return new BoolValue(false);
  }
}
