package com.company.cache.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class StorageTest {

    @Parameterized.Parameters(name = "{index}")
    public static Iterable<Object> data() throws IOException {
        return Arrays.asList(new Object[]{
                new HashMapStorage<Integer, String>(3),
                new FileSystemStorage<Integer, String>(3, Paths.get("./"))
        });
    }

    private Storage<Integer, String> storage;

    public StorageTest(Storage<Integer, String> storage) {
        this.storage = storage;
    }

    @Test
    public void shouldPutAnObjectToStorage() {
        storage.put(1,"0");
        assertThat(storage.size(), is(1));
    }

    @Test
    public void shouldGetAnObjectFromStorage() {
        storage.put(0,"0");
        assertThat(storage.get(0),is(Optional.of("0")));
    }

    @Test
    public void shouldGetOptionalEmptyFromStorageIfKeyNotContainsInStorage() {
        assertThat(storage.get(10), is(Optional.empty()));
    }

    @Test
    public void shouldRemoveAnObjectFromStorage() {
        storage.put(0,"0");
        assertThat(storage.size(), is(1));
        storage.remove(0);
        assertThat(storage.size(), is(0));
    }

    @Test
    public void shouldClearStorage() {
        storage.put(0,"0");
        storage.put(1,"1");
        storage.clear();
        assertThat(storage.size(), is(0));
    }

    @Test
    public void shouldContainsKey() {
        storage.put(0,"0");
        assertTrue(storage.containsKey(0));
    }

    @Test
    public void shouldNotContainsAKeyIfItIsNotInTheStorage() {
        storage.put(0, "0");
        assertFalse(storage.containsKey(100));
    }
}
