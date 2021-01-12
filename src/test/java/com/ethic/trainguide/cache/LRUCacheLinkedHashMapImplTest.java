package com.ethic.trainguide.cache;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class LRUCacheLinkedHashMapImplTest {

    @Test
    public void testPutItem() {
        LRUCache<Integer, String> cache = new LRUCacheLinkedHashMapImpl(3);

        cache.putItem(1, "one");
        cache.putItem(2, "two");
        cache.putItem(3, "three");

        assertTrue(cache.getItem(1) != null);
        assertTrue(cache.getItem(2) != null);
        assertTrue(cache.getItem(3) != null);
    }

    @Test
    public void testSetCapacity() {
        LRUCache<Integer, String> cache = new LRUCacheLinkedHashMapImpl(3);

        cache.setCapacity(2);
        assertTrue(cache.getCapacity() == 2);
    }

    @Test
    public void testLRUItemIsRemoved() {
        LRUCache<Integer, String> cache = new LRUCacheLinkedHashMapImpl(3);

        cache.putItem(1, "one");
        cache.putItem(2, "two");
        cache.putItem(3, "three");

        assertTrue(cache.getItem(1) != null);
        assertTrue(cache.getItem(2) != null);
        assertTrue(cache.getItem(3) != null);

        cache.putItem(4, "four");
        assertTrue(cache.getItem(1) == null);
        assertTrue(cache.getItem(4) != null);

        cache.putItem(5, "five");
        assertTrue(cache.getItem(2) == null);
        assertTrue(cache.getItem(5) != null);
    }
}
