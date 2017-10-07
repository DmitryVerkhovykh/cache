package com.company.cache.storage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FileSystemStorage<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private final Path path;
    private final int capacity;
    private final Map<K, String> keyToPathStorage;

    public FileSystemStorage(int capacity,Path path) {
        try {
            this.path = Files.createTempDirectory(path,"");
            this.path.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.capacity = capacity;
        this.keyToPathStorage = new HashMap<>(capacity, 1.1f);
    }

    @Override
    public void put(K key, V value) {
        try {
            File file = Files.createTempFile(path, "", "").toFile();
            try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))){
                outputStream.writeObject(value);
                outputStream.flush();
                keyToPathStorage.put(key, file.getName());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> result = Optional.empty();
        String fileName = keyToPathStorage.get(key);
        try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(path + File.separator + fileName)))) {
            result = Optional.of((V)inputStream.readObject());
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    @Override
    public void remove(K key) {
        String fileName = keyToPathStorage.get(key);
        File file = new File(path + File.separator + fileName);
        file.delete();
        keyToPathStorage.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return keyToPathStorage.containsKey(key);
    }

    @Override
    public void clear() {
        try {
            Files.walk(path).filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
            keyToPathStorage.clear();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    public int size() {
        return keyToPathStorage.size();
    }
}
