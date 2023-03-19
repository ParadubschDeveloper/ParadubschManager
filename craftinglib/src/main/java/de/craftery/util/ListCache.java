package de.craftery.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple cache for lists, to be used in combination with databases.
 * @param <K> Type of key for a cache
 * @param <T> The array type of the cached element
 */
public class ListCache<K, T> {
    private final Map<K, List<T>> cache = new HashMap<>();
    public void invalidate(K key) {
        cache.remove(key);
    }

    public void cache(K key, List<T> value) {
        cache.put(key, value);
    }

    public List<T> get(K key) {
        return cache.get(key);
    }

    public boolean isValid(K key) {
        return cache.containsKey(key);
    }
}
