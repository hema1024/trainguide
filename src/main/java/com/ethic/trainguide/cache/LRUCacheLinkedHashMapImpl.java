package com.ethic.trainguide.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple LRU cache implemented using LinkedHashMap to
 * cache train routes with shortest paths computed,
 * so we dont have to recompute the shortest paths
 * if the origin station is the same
 * @param <K>
 * @param <V>
 */
public class LRUCacheLinkedHashMapImpl<K, V> extends LinkedHashMap<K, V> implements LRUCache<K, V> {
    private int capacity;

    public LRUCacheLinkedHashMapImpl(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public void putItem(K key, V value) {
        put(key, value);
    }

    @Override
    public V getItem(K key) {
        return get(key);
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
