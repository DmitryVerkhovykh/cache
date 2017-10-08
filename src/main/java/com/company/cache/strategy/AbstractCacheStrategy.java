package com.company.cache.strategy;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCacheStrategy<K> implements Strategy<K> {
    private final Map<K, Long> storage;

    public AbstractCacheStrategy() {
        storage = new HashMap<>();
    }

    public Map<K, Long> getStorage() {
        return storage;
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }
}
