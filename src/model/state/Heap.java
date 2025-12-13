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

        // 1. Start with addresses directly referenced in the symbol table
        java.util.Set<Integer> reachable = new java.util.HashSet<>(symTableAddr);

        boolean changed = true;

        while (changed) {
            // 2. For each known reachable address, get the values stored at those addresses
            java.util.List<Integer> current = new java.util.ArrayList<>(reachable);

            // All values reachable by current set
            //Example: if reachable = {2}, and heap has 2 → (1, int)
            //we pull out the value (1, int)
            java.util.List<Value> reachableValues =
                    memory.entrySet().stream()
                            .filter(e -> current.contains(e.getKey()))
                            .map(java.util.Map.Entry::getValue)
                            .toList();

            // 3. From those values extract all addresses referenced INSIDE the heap
            //Example: (1, int) → address 1
            java.util.List<Integer> nextLevel =
                    IHeap.getAddrFromValues(reachableValues);

            // 4. Add newly discovered addresses
            changed = reachable.addAll(nextLevel);
        }

        // 5. Keep only reachable heap entries
        java.util.Map<Integer, Value> newMemory =
                memory.entrySet().stream()
                        .filter(e -> reachable.contains(e.getKey()))
                        .collect(java.util.stream.Collectors.toMap(
                                java.util.Map.Entry::getKey,
                                java.util.Map.Entry::getValue
                        ));
        // 6. Replace old heap with cleaned heap
        memory.clear();
        memory.putAll(newMemory);
    }
}
