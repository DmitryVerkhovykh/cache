package com.company.cache.strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MRUCahceStrategyTest {

    private MRUCacheStrategy<Integer> cacheStrategy;

    @Before
    public void setUp() {
        cacheStrategy = new MRUCacheStrategy<>();
    }

    @After
    public void tearDown() {
        cacheStrategy = null;
    }

    @Test
    public void shouldDisplaceTheNewestObject() {
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(),is(4));
        cacheStrategy.putObject(0);
        assertThat(cacheStrategy.getStorage().size(),is(4));
        assertThat(cacheStrategy.getReplacedKey().get(), is(0));
    }

    @Test
    public void shouldNotChangeSizeOfCache() {
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(),is(4));
        IntStream.range(0, 4).forEach(i -> cacheStrategy.putObject(i));
        assertThat(cacheStrategy.getStorage().size(), is(4));
    }
}

