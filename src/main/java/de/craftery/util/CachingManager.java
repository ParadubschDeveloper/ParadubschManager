package de.craftery.util;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CachingManager {
    @Getter
    private final Map<String, Map<Serializable, Object>> cache = new HashMap<>();
    public <T extends BaseDatabaseEntity<?, ?>> void cacheEntity(Class<T> clazz, T entity, Serializable id) {
        Map<Serializable, Object> clazzCache = cache.get(clazz.getName());
        if (clazzCache == null) {
            clazzCache = new HashMap<>();
        }
        clazzCache.put(id, entity);
        cache.put(clazz.getName(), clazzCache);
    }

    public void cacheEntity(Class<?> clazz, Object entity, Serializable id) {
        Map<Serializable, Object> clazzCache = cache.get(clazz.getName());
        if (clazzCache == null) {
            clazzCache = new HashMap<>();
        }
        clazzCache.put(id, entity);
        cache.put(clazz.getName(), clazzCache);
    }

    public <T extends BaseDatabaseEntity<?, ?>> T getEntity(Class<T> clazz, Serializable id) {
        Map<Serializable, Object> clazzCache = cache.get(clazz.getName());
        if (clazzCache == null) {
            return null;
        }
        Object entity = clazzCache.get(id);
        return clazz.cast(entity);
    }

    public <T extends BaseDatabaseEntity<?, ?>> void deleteEntity(Class<T> clazz, Serializable id) {
        Map<Serializable, Object> clazzCache = cache.get(clazz.getName());
        if (clazzCache == null) {
            return;
        }
        clazzCache.remove(id);
        if (clazzCache.size() == 0) {
            cache.remove(clazz.getName());
        } else {
            cache.put(clazz.getName(), clazzCache);
        }

    }
}
