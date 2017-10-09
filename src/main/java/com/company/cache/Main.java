package com.company.cache;

import com.company.cache.storage.FileSystemStorage;
import com.company.cache.storage.HashMapStorage;
import com.company.cache.storage.Storage;
import com.company.cache.strategy.LRUCacheStrategy;
import com.company.cache.strategy.MRUCacheStrategy;
import com.company.cache.strategy.Strategy;

import java.nio.file.Paths;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int fsCapacity = 3;
        Storage<Integer, String> fsStorage = new FileSystemStorage<>(fsCapacity, Paths.get("./"));
        Strategy<Integer> sfStrategy = new LRUCacheStrategy<>();
        Cache<Integer, String> fsCache = new CacheLevel<>(fsStorage, sfStrategy, fsCapacity);

        int memCapacity = 5;
        Storage<Integer, String> memStorage = new HashMapStorage<>(memCapacity);
        Strategy<Integer> memStrategy = new MRUCacheStrategy<>();
        Cache<Integer, String> twoLevCache = new CacheLevel<Integer, String>(memStorage, memStrategy, fsCache, memCapacity);

        IntStream.range(0, 5).forEach(i -> twoLevCache.cache(i, "Value " + i));
        twoLevCache.cache(0, "Value 0");

        IntStream.range(5, 9).forEach( i -> twoLevCache.cache(i, "Value " + i));

        twoLevCache.remove(4);

        String str = twoLevCache.retrieve(7).get();
        System.out.println(str);
        System.out.println(twoLevCache.size());
        twoLevCache.clear();
        System.out.println(twoLevCache.size());
    }
}
