package com.company.cache.strategy;

import java.util.Map;
import java.util.Optional;

public class MRUCacheStrategy<K> extends AbstractCacheStrategy<K> {
    @Override
    public Optional<K> getReplacedKey() {
        Optional<K> result = getStorage().entrySet().stream()
                .sorted(Map.Entry.<K, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey()).findFirst();
        return result;
    }

    @Override
    public void putObject(K key) {
        getStorage().put(key, System.nanoTime());
    }
}
