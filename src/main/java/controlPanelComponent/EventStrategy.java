package controlPanelComponent;

import models.Events;

/**
 * Interface for Strategy Pattern
 *
 * @author Gheorghe Mironica
 */
public interface EventStrategy {

    /**
     * Abstract strategy to create Events
     * @return {@link Events}
     */
    Events createEvent();
}
