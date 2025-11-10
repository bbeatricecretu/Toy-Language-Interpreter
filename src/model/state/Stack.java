package model.state;

import model.exception.OperationExceptions;

import java.util.ArrayDeque;
import java.util.Deque;

public class Stack<T> implements IStack<T> {
    Deque<T> stack;

    public Stack() {
        this.stack = new ArrayDeque<>();
    }

    @Override
    public void push(T value) {
        stack.push(value);
    }

    @Override
    public T pop() {
        if (stack.isEmpty()) {
            throw new OperationExceptions("Stack is empty");
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        if (stack.isEmpty()) return "(empty)";

        StringBuilder sb = new StringBuilder();
        for (T elem : stack) {
            sb.append("  ").append(elem.toString()).append("\n");
        }
        return sb.toString();
    }
}

