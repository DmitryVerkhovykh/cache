package com.company.cache;

import com.company.cache.storage.FileSystemStorage;
import com.company.cache.storage.HashMapStorage;
import com.company.cache.storage.Storage;
import com.company.cache.strategy.LRUCacheStrategy;
import com.company.cache.strategy.Strategy;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Storage<Integer, String> storage = new HashMapStorage<>(3);
        Storage<Integer, String> fileSystemStorage = new FileSystemStorage<>(3, Paths.get("D:/Workspace/Projects/Java/cache/"));
        Strategy<Integer> strategy = new LRUCacheStrategy<>();
        Cache<Integer, String> cache = new CacheLevel<Integer, String>(fileSystemStorage,strategy,3);
        for(int i = 0; i < 3; i++)
            cache.cache(i, "Value " + i);
        cache.cache(0, "Value 0");
        cache.cache(4, "Value 4");
    }
}
