package com.company.cache;

import com.company.cache.storage.Storage;
import com.company.cache.strategy.Strategy;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheLevel<K extends Serializable, V extends Serializable> implements Cache<K, V> {

    private final ReadWriteLock lock;
    private final Storage<K, V> storage;
    private final Strategy<K> strategy;
    private final Optional<Cache<K, V>> cache;
    private final int capacity;

    public CacheLevel(Storage<K, V> storage, Strategy<K> strategy, Cache<K, V> cache, int capacity) {
        this.storage = storage;
        this.strategy = strategy;
        this.cache = Optional.ofNullable(cache);
        this.capacity = capacity;
        lock = new ReentrantReadWriteLock();
    }

    public CacheLevel(Storage<K, V> storage, Strategy<K> strategy, int capacity) {
        this.storage = storage;
        this.strategy = strategy;
        cache = Optional.empty();
        lock = new ReentrantReadWriteLock();
        this.capacity = capacity;
    }

    @Override
    public void cache(K key, V value) {
        lock.writeLock().lock();
        if (storage.size() >= capacity && !storage.containsKey(key)) {
            K rep = strategy.getReplacedKey().orElseThrow(IllegalAccessError::new);
            V val = storage.get(rep).orElseThrow(IllegalStateException::new);
            cache.ifPresent(cache -> cache.cache(rep, val));
            storage.remove(rep);
            strategy.remove(rep);
        }
        storage.put(key, value);
        strategy.putObject(key);
        lock.writeLock().unlock();
    }

    @Override
    public Optional<V> retrieve(K key) {
        lock.readLock().lock();
        Optional<V> result;
        if(storage.containsKey(key))
            result = storage.get(key);
        else
            result = cache.map(cache -> cache.retrieve(key)).orElse(Optional.empty());
        lock.readLock().unlock();
        return result;
    }

    @Override
    public void remove(K key) {
        lock.writeLock().lock();
        if(storage.containsKey(key)) {
            storage.remove(key);
            strategy.remove(key);
        } else
            cache.ifPresent(cache -> cache.remove(key));
        lock.writeLock().unlock();
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        storage.clear();
        strategy.clear();
        cache.ifPresent(cache -> cache.clear());
        lock.writeLock().unlock();
    }

    @Override
    public int size() {
        lock.readLock().lock();
        int size = storage.size() + cache.map(Cache::size).orElse(0);
        lock.readLock().unlock();
        return size;
    }
}
