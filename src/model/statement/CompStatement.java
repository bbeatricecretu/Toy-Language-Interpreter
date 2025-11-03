package model.statement;

import model.state.ProgramState;

public record CompStatement(Statement first, Statement second) implements Statement {

  @Override
  public String toString() {
    return "(" + first.toString() + "; " + second.toString() + ")";
  }

  @Override
  public ProgramState execute(ProgramState state) {
    state.executionStack().push(second);
    state.executionStack().push(first);
    return state;
  }
}
