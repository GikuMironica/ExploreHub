package handlers;

import javafx.scene.image.Image;

/**
 * A singleton class tha contains an LRU cache for storing images.s
 * @author Hidayat Rzayev
 */
public class CacheSingleton {

    private static CacheSingleton instance;

    private LRUCache<Integer, Image> imageCache;

    private CacheSingleton() {
        imageCache = new LRUCache<>(50);
    }

    public static CacheSingleton getInstance() {
        if (instance == null) {
            instance = new CacheSingleton();
        }
        return instance;
    }

    /**
     * Adds a new image to the cache.
     *
     * @param eventId - the event whose image is to be cached
     * @param image - image that is to be cached
     */
    public void putImage(int eventId, Image image) {
        imageCache.put(eventId, image);
    }

    /**
     * Returns a cached image of the given event.
     *
     * @param eventId - the event whose image is to be fetched
     * @return the corresponding {@link Image} object
     */
    public Image getImage(int eventId) {
        return imageCache.get(eventId);
    }

    /**
     * Checks if the cache contains the image for the given event.
     *
     * @param eventId - the event that is to be checked
     * @return {@code true} if the cache contains the image, {@code false} otherwise.
     */
    public boolean containsImage(int eventId) {
        return imageCache.containsKey(eventId);
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        imageCache.clear();
    }
}
