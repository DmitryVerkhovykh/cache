package com.company.cache.storage;

import java.io.Serializable;
import java.util.Optional;

public interface Storage<K extends Serializable, V extends Serializable> {
    void put(K key, V value);
    Optional<V> get(K key);
    void remove(K key);
    boolean containsKey(K key);
    void clear();
    int size();
}
