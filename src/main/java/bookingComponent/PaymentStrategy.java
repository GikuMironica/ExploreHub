package bookingComponent;

/**
 * Payment Strategy
 * @author Domagoj Frecko
 */

public interface PaymentStrategy {

    /**
     * Method that processes the booking of an event.
     * @return true if successful
     */
    boolean pay();

}
