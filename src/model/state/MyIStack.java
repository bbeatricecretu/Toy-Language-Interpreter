package model.state;

public interface MyIStack<T> {
  void push(T value);

  T pop();

  boolean isEmpty();
}
