package model.statement;

import model.state.ProgramState;

public class NoOpStatement implements Statement {

  @Override
  public String toString() {
    return "NoOperationStatement";
  }

  @Override
  public ProgramState execute(ProgramState state) {
    return state;
  }
}
