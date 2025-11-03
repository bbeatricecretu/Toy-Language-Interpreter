package model.state;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
  private final List<T> list;

  public MyList() {
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
