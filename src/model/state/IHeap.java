package model.state;

import model.value.RefValue;
import model.value.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface IHeap {
    int allocate(Value value);

    Value getValue(int address);

    void update(int address, Value value);

    boolean isAllocated(int address);

    Map<Integer, Value> getContent();

    void safeGarbageCollector(List<Integer> symTableAddr);

    static List<Integer> getAddrFromValues(Collection<Value> values) {
        return values.stream()
                .filter(RefValue.class::isInstance)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }
}
