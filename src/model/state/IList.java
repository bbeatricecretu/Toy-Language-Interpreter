package model.state;

public interface IList<T> {
    void add(T value);

    java.util.List<T> getContent();

}
