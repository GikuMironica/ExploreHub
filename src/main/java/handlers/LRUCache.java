package handlers;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class implements the "Least Recently Used (LRU) Cache" data structure.
 * It is intended for caching the images taken from the DB for optimization purposes.
 * The cached images will be taken from this class without having to access the database.
 * @author Hidayat Rzayev
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private LRUCache<K, V> instance;
    private int maxSize;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75F, true);
        this.maxSize = maxSize;
    }

    /**
     * This method is internally being called when calling the {@link LinkedHashMap} class's
     * put() method.
     *
     * It indicates whether or not the least recently used entry should be removed from the cache.
     * If the current size of the cache is greater than the specified maximum size, then the
     * LRU entry will be removed before a new entry can be inserted into the cache.
     *
     * @param eldest - the least recently used entry
     * @return {@code true} if the eldest entry should be removed
     *         from the cache; {@code false} if it should be retained.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
