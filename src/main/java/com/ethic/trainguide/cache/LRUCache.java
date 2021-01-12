package com.ethic.trainguide.cache;

/**
 * Interface for LRU (least recently used) cache.
 * Implementation should keep in memory only as many elements
 * in the cache as set by setCapacity().
 * @param <K>
 * @param <V>
 */
public interface LRUCache<K, V> {

    /**
     * Put an item into the cache
     * @param key
     * @param value
     */
    public void putItem(K key, V value);

    /**
     * Get an item from the cache.
     * @param key key of the item to fetch
     * @return null if item does not exist
     */
    public V getItem(K key);

    /**
     * Maximum capacity of the cache.
     * Implementation should keep in memory only as many elements
     * in the cache as set by this value
     * @param capacity
     */
    public void setCapacity(int capacity);

    /**
     * Get maximum capacity of the cache
     */
    public int getCapacity();

}
