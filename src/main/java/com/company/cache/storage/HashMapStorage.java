package com.company.cache.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HashMapStorage<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private final Map<K, V> storage;
    private final int capacity;

    public HashMapStorage(int capacity) {
        this.capacity = capacity;
        storage = new HashMap<>(capacity);
    }

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(storage.get(key));
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return storage.containsKey(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
