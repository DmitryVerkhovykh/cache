package com.company.cache;

import com.company.cache.storage.Storage;
import com.company.cache.strategy.Strategy;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

public class CacheLevelTest {
    @Rule
    public EasyMockRule em = new EasyMockRule(this);

    @Mock(MockType.DEFAULT)
    private Storage<Integer, String> storage;

    @Mock(MockType.DEFAULT)
    private Strategy<Integer> strategy;


    @TestSubject
    private Cache<Integer, String> cache = new CacheLevel<>(storage, strategy, 3);

    @Before
    public void SetUp() {
       cache = new CacheLevel<Integer, String>(storage, strategy,3);
    }

    @Test
    public void shouldCacheAnObjectIfCacheSizeLessThatCacheCapacity() {
        storage.put(0, "0");
        expectLastCall().times(1);
        expect(storage.size()).andReturn(0);
        strategy.putObject(0);
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.cache(0, "0");
        verify(storage, strategy);
    }

    @Test
    public void shouldCacheAnObjectIfCacheSizeEqualToOrGreaterThatCacheCapacityAndCacheContainsKey() {
        storage.put(0, "0");
        expectLastCall().times(1);
        expect(storage.size()).andReturn(3);
        expect(storage.containsKey(0)).andReturn(true);
        strategy.putObject(0);
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.cache(0, "0");
        verify(storage, strategy);
    }

    @Test
    public void shouldCacheAnObjectIfCacheSizeEqualToOrGreterThatCacheCapacityAndCacheNotContainsKey() {
        expect(storage.size()).andReturn(3);
        expect(storage.containsKey(0)).andReturn(false);
        expect(strategy.getReplacedKey()).andReturn(Optional.of(1));
        expect(storage.get(1)).andReturn(Optional.of("1"));
        storage.remove(1);
        expectLastCall().times(1);
        strategy.remove(1);
        expectLastCall().times(1);
        storage.put(0, "0");
        expectLastCall().times(1);
        strategy.putObject(0);
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.cache(0, "0");
        verify(strategy, storage);
    }

    @Test
    public void shouldRetrieveOptionalFromCacheIfCacheContainsKey() {
        expect(storage.containsKey(0)).andReturn(true);
        expect(storage.get(0)).andReturn(Optional.of("0"));
        replay(storage);
        assertThat(cache.retrieve(0), is(Optional.of("0")));
        verify(storage);
    }

    @Test
    public void shouldRetrieveOptinalEmptyFromCacheIfCacheNotContainsKey() {
        expect(storage.containsKey(0)).andReturn(false);
        replay(storage);
        assertThat(cache.retrieve(0), is(Optional.empty()));
        verify(storage);
    }

    @Test
    public void shouldRemoveObjectFromCache() {
        storage.remove(1);
        expectLastCall().times(1);
        strategy.remove(1);
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.remove(1);
        verify(storage, strategy);
    }

    @Test
    public void shouldClearCache() {
        storage.clear();
        expectLastCall().times(1);
        strategy.clear();
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.clear();
        verify(storage, strategy);
    }
}
