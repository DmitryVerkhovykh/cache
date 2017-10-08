package com.company.cache.strategy;

public interface Strategy<K> {
    K getReplacedKey();
    void putObject(K key);
    void remove(K key);
}
/* дублирование кода в getReplacedKey, NPE возможен там же в result.get();
добавить в интерфейс метод remove
 */