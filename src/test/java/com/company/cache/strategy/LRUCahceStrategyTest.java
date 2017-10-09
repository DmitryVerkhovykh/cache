package com.company.cache.strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.util.stream.IntStream;

public class  LRUCahceStrategyTest {

    private LRUCacheStrategy<Integer> cacheStrategy;

    @Before
    public void setUp() {
        cacheStrategy = new LRUCacheStrategy<>();
    }

    @After
    public void tearDown() {
        cacheStrategy = null;
    }

    @Test
    public void shouldDisplaceTheOldestObject() {
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(),is(4));
        cacheStrategy.putObject(0);
        assertThat(cacheStrategy.getStorage().size(),is(4));
        assertThat(cacheStrategy.getReplacedKey().get(), is(1));

    }

    @Test
    public void shouldNotChangeSizeOfCache() {
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(),is(4));
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(), is(4));
    }

}
