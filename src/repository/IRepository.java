package repository;

import model.state.ProgramState;
import model.exception.FileException;

import java.util.List;

public interface IRepository {
    List<ProgramState> getPrgList();

    void setPrgList(List<ProgramState> list);

    void logPrgStateExec(ProgramState prg) throws FileException;
}