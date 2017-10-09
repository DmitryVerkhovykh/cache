package com.company.cache;

import com.company.cache.storage.FileSystemStorage;
import com.company.cache.storage.Storage;
import com.company.cache.strategy.LRUCacheStrategy;
import com.company.cache.strategy.Strategy;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Storage<Integer, String> fsStorage = new FileSystemStorage<>(3, Paths.get("./"));
        Strategy<Integer> sfStrategy = new LRUCacheStrategy<>();
    }
}
