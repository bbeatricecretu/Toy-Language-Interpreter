package model.state;

import java.util.ArrayList;

public class List<T> implements IList<T> {
    private final java.util.List<T> list;

    public List() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T value) {
        list.add(value);
    }

    @Override
    public String toString() {
        if (list.isEmpty()) return "(empty)";

        StringBuilder sb = new StringBuilder();
        for (T elem : list) {
            sb.append("  ").append(elem.toString()).append("\n");
        }
        return sb.toString();
    }
}
