package repository;

import model.state.ProgramState;
import model.exception.FileException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Repo implements IRepository {
    private final List<ProgramState> programs;
    private final String logFilePath;

    public Repo(List<ProgramState> initProgram, String logFilePath) {
        this.programs = initProgram;
        this.logFilePath = logFilePath;

        //Clear previous log content
        try (var writer = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, false))))  //BufferedWriter stores text in memory before flushing to disk, append mode is false
        {
        } catch (IOException e) {
            throw new FileException("Could not initialize log file: " + logFilePath);
        }

    }

    @Override
    public ProgramState getCrtPrg() {
        if (programs.isEmpty()) {
            throw new IllegalStateException("No programs to run in repository");
        }
        return programs.getFirst();
    }

    @Override
    public void logPrgStateExec() throws FileException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            int count = 1;
            for (ProgramState prg : programs) {
                logFile.printf("Program %d:%n", count++);
                logFile.println(prg.toString());   // uses ProgramState.toString()
                logFile.println("\n===============================Next Statement====================================\n");
            }
        } catch (IOException e) {
            throw new FileException("Error writing to log file: " + logFilePath);
        }
    }


}
