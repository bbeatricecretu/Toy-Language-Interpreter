package model.state;

import model.exception.FileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * FileTable manages all opened files for the Toy Language interpreter.
 * It maps a file name (String) to its BufferedReader instance.
 * Each filename must be unique.
 */
public class FileTable implements IFileTable {
    private final Map<String, BufferedReader> files = new HashMap<>();

    @Override
    public boolean isOpened(String fileName) {
        return files.containsKey(fileName);
    }

    @Override
    public void add(String fileName, BufferedReader reader) {
        if (isOpened(fileName)) {
            throw new FileException("File already exists: " + fileName);
        }
        files.put(fileName, reader);
    }

    @Override
    public BufferedReader getFile(String fileName) {
        if (!isOpened(fileName)) {
            throw new FileException("File is not opened: " + fileName);
        }
        return files.get(fileName);
    }

    @Override
    public void delete(String fileName) {
        if (isOpened(fileName)) {
            try {
                files.get(fileName).close();
            } catch (IOException e) {
                throw new FileException("Error closing file: " + fileName);
            }
            files.remove(fileName);
        } else {
            // safe remove (ignore if file was not open)
            files.remove(fileName);
        }
    }

    @Override
    public String toString() {
        if (files.isEmpty()) {
            return "(empty)";
        }

        StringBuilder result = new StringBuilder();
        for (String fileName : files.keySet()) {
            result.append("  ").append(fileName).append("\n");
        }
        return result.toString();
    }

    @Override
    public Map<String, BufferedReader> getContent() {
        return this.files; // Returns the internal HashMap of opened files
    }
}
