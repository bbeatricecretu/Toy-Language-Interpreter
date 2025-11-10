package repository;

import model.state.ProgramState;
import model.exception.FileException;

public interface IRepository {
    ProgramState getCrtPrg();

    void logPrgStateExec();
}
