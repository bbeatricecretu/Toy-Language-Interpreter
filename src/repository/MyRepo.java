package repository;

import model.state.ProgramState;

import java.util.List;

public class MyRepo implements IRepository {
  private final List<ProgramState> programs;

  public MyRepo(List<ProgramState> initProgram) {
    programs = initProgram;
  }

  @Override
  public ProgramState getCrtPrg()  {
    if (programs.isEmpty()) {
      throw new IllegalStateException("No programs to run in repository");
    }
      return programs.getFirst();
  }
}
