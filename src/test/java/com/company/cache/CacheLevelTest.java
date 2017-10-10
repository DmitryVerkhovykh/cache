package com.company.cache;

import com.company.cache.storage.Storage;
import com.company.cache.strategy.Strategy;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.easymock.TestSubject;
import org.junit.After;
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

    @Mock
    private Cache<Integer, String> innerCache;


    @TestSubject
    private Cache<Integer, String> cache = new CacheLevel<>(storage, strategy, innerCache,3);

    @Before
    public void SetUp() {
       cache = new CacheLevel<Integer, String>(storage, strategy,innerCache, 3);
    }

    @After
    public void tearDown() {
        cache = null;
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
        innerCache.cache(1, "1");
        expectLastCall().times(1);
        replay(storage, strategy, innerCache);
        cache.cache(0, "0");
        verify(strategy, storage, innerCache);
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
    public void shouldRetrieveOptionalFromCacheIfInnerCacheContainsKey() {
        expect(storage.containsKey(0)).andReturn(false);
        expect(innerCache.retrieve(0)).andReturn(Optional.of("0"));
        replay(storage, innerCache);
        assertThat(cache.retrieve(0), is(Optional.of("0")));
        verify(storage);
    }

    @Test
    public void shouldRetrieveOptinalEmptyFromCacheIfAllLevelsOfCacheNotContainsKey() {
        expect(storage.containsKey(0)).andReturn(false);
        expect(innerCache.retrieve(0)).andReturn(Optional.empty());
        replay(storage, innerCache);
        assertThat(cache.retrieve(0), is(Optional.empty()));
        verify(storage, innerCache);
    }

    @Test
    public void shouldRemoveObjectFromCacheIfCacheContainsKey() {
        expect(storage.containsKey(1)).andReturn(true);
        storage.remove(1);
        expectLastCall().times(1);
        strategy.remove(1);
        expectLastCall().times(1);
        replay(storage, strategy);
        cache.remove(1);
        verify(storage, strategy);
    }

    @Test
    public void shouldRemoveObjectFromCacheIfCacheNotContainsKey() {
        expect(storage.containsKey(1)).andReturn(false);
        innerCache.remove(1);
        expectLastCall().times(1);
        replay(innerCache);
        cache.remove(1);
        verify(innerCache);
    }

    @Test
    public void shouldClearCache() {
        storage.clear();
        expectLastCall().times(1);
        strategy.clear();
        expectLastCall().times(1);
        innerCache.clear();
        expectLastCall().times(1);
        replay(storage, strategy, innerCache);
        cache.clear();
        verify(storage, strategy, innerCache);
    }

    @Test
    public void shouldReturnSize() {
        expect(storage.size()).andReturn(2);
        expect(innerCache.size()).andReturn(2);
        replay(storage, innerCache);
        assertThat(cache.size(), is(4));
        verify(storage, innerCache);
    }
}
