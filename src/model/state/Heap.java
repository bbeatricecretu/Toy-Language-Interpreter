package model.state;

import model.value.RefValue;
import model.value.Value;
import model.state.IHeap;

import java.util.*;
import java.util.stream.Collectors;

public class Heap implements IHeap {

    private final Map<Integer, Value> memory;
    private int nextFreeAddress;

    public Heap() {
        this.memory = new HashMap<>();
        this.nextFreeAddress = 1;  // addresses start from 1
    }

    @Override
    public int allocate(Value value) {
        int newAddress = nextFreeAddress;
        nextFreeAddress++;
        memory.put(newAddress, value);
        return newAddress;
    }

    @Override
    public Value getValue(int address) {
        return memory.get(address);
    }

    @Override
    public void update(int address, Value value) {
        memory.put(address, value);
    }

    @Override
    public boolean isAllocated(int address) {
        return memory.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return memory;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        memory.forEach((key, val) ->
                result.append(key).append(" -> ").append(val).append("\n")
        );
        return result.toString();
    }

    @Override
    public void safeGarbageCollector(java.util.List<Integer> symTableAddr) {

        java.util.Set<Integer> reachable = new java.util.HashSet<>(symTableAddr);

        boolean changed = true;

        while (changed) {
            java.util.List<Integer> current = new java.util.ArrayList<>(reachable);

            // All values reachable by current set
            java.util.List<Value> reachableValues =
                    memory.entrySet().stream()
                            .filter(e -> current.contains(e.getKey()))
                            .map(java.util.Map.Entry::getValue)
                            .toList();

            java.util.List<Integer> nextLevel =
                    IHeap.getAddrFromValues(reachableValues);

            changed = reachable.addAll(nextLevel);
        }

        java.util.Map<Integer, Value> newMemory =
                memory.entrySet().stream()
                        .filter(e -> reachable.contains(e.getKey()))
                        .collect(java.util.stream.Collectors.toMap(
                                java.util.Map.Entry::getKey,
                                java.util.Map.Entry::getValue
                        ));

        memory.clear();
        memory.putAll(newMemory);
    }
}
