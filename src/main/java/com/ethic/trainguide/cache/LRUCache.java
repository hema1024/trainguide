package com.ethic.trainguide.cache;

public interface LRUCache<K, V> {

    public void putItem(K key, V value);

    public V getItem(K key);

    public void setCapacity(int capacity);

}
