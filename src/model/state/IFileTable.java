package model.state;

import java.io.BufferedReader;
import java.util.Map;

public interface IFileTable {
    boolean isOpened(String fileName);

    void add(String fileName, BufferedReader reader);

    BufferedReader getFile(String fileName);

    public void delete(String fileName);

    Map<String, BufferedReader> getContent();
}
