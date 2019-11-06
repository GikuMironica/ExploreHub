package filterComponent;

import javafx.collections.ObservableList;
import models.Events;

/**
 *Interface that represents the criteria for filter.
 * @author Aleksejs Marmiss
 *
 */
public interface Criteria {
    /**
     *Interface method that checks which events meet the criteria. If event does not meet criteria, it is removed from the list.
     * @param events Observable list of event.
     *
     */
    public ObservableList<Events> meetCriteria(ObservableList<Events> events);
}
