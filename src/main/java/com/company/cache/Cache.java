package com.company.cache;

import java.util.Optional;

public interface Cache<K, V> {
    void cache(K key, V value);
    Optional<V> retrieve(K key);
    void remove(K key);
    void clear();
    int size();
}
