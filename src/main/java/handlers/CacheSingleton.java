package handlers;

import javafx.scene.image.Image;
import models.Events;

import java.util.List;

/**
 * A singleton class tha contains 3 caches for storing images, boolean values to check
 * whether or not a particular event is contained in the wishlist, as well as boolean
 * values to check whether or not a particular event has already been booked.
 * @author Hidayat Rzayev
 */
public class CacheSingleton {

    private static CacheSingleton instance;

    private LRUCache<Integer, Image> imageCache;
    private LRUCache<Integer, Boolean> eventInWishlistCache;
    private LRUCache<Integer, Boolean> eventBookedCache;

    private CacheSingleton() {
        imageCache = new LRUCache<>(20);
        eventInWishlistCache = new LRUCache<>(20);
        eventBookedCache = new LRUCache<>(20);
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
     * Adds a new boolean value to the cache, which indicates whether
     * or not the given event is contained in the wishlist.
     *
     * @param eventId - the event whose boolean values is to be cached
     * @param inWishlist - the boolean value that is to be cached
     */
    public void putEventInWishlist(int eventId, boolean inWishlist) {
        eventInWishlistCache.put(eventId, inWishlist);
    }

    /**
     * Checks if the given event is contained in the wishlist.
     *
     * @param eventId - the event that is to be checked
     * @return {@code true} if the event is in the wishlist, {@code false} otherwise.
     */
    public boolean isEventInWishlist(int eventId) {
        return eventInWishlistCache.get(eventId);
    }

    /**
     * Adds a new boolean value to the cache, which indicates whether
     * or not the given event has already been booked.
     *
     * @param eventId - the event whose boolean values is to be cached
     * @param booked - the boolean values that is to be cached
     */
    public void putEventBooked(int eventId, boolean booked) {
        eventBookedCache.put(eventId, booked);
    }

    /**
     * Checks if the given event is already booked.
     *
     * @param eventId - the event that is to be checked
     * @return {@code true} if the event is already booked, {@code false} otherwise.
     */
    public boolean isEventBooked(int eventId) {
        return eventBookedCache.get(eventId);
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
     * Checks if the cache contains the boolean value, which indicates
     * whether or not a particular event is contained in the wishlist, fot the given event.
     *
     * @param eventId - the event that is to be checked
     * @return {@code true} if the cache contains the boolean value, {@code false} otherwise.
     */
    public boolean containsEventInWishlist(int eventId) {
        return eventInWishlistCache.containsKey(eventId);
    }

    /**
     * Checks if the cache contains the boolean value, which indicates
     * whether or not a particular event is already booked, fot the given event.
     *
     * @param eventId - the event that is to be checked
     * @return {@code true} if the cache contains the boolean value, {@code false} otherwise.
     */
    public boolean containsEventBooked(int eventId) {
        return eventBookedCache.containsKey(eventId);
    }

    public void updateCache(List<Events> events) {
        for (Events event : events) {
            int eventId = event.getId();
            updateImage(event, eventId);
        }
    }

    private void updateImage(Events event, int eventId) {
        if (this.containsImage(eventId)) {
            String imageURL = event.getPicture().getLogo();
            Image eventLogo = new Image(imageURL);
            this.putImage(eventId, eventLogo);
        }
    }

//    private void updateEventInWishlist(Events event, int eventId) {
//        if (this.containsEventInWishlist(eventId)) {
//            boolean inWishlist = event.get
//            Image eventLogo = new Image(imageURL);
//            this.putImage(eventId, eventLogo);
//        }
//    }
}
