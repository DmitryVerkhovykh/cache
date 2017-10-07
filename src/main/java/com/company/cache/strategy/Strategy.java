package com.company.cache.strategy;

public interface Strategy<K> {
    K getReplacedKey();
    void putObject(K key);
}
