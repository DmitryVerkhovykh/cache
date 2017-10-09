package com.company.cache.strategy;

import java.util.Optional;

public interface Strategy<K> {
    Optional<K> getReplacedKey();
    void putObject(K key);
    void remove(K key);
    void clear();
}
