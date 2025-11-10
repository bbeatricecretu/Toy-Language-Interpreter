package model.state;

public interface IStack<T> {
    void push(T value);

    T pop();

    boolean isEmpty();
}
