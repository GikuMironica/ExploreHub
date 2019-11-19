package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *Class that implements the Free Places criteria for filter.
 * @author Aleksejs Marmiss
 *
 */
public class FreePlacesCriteria implements Criteria {

    private int nrOfPlaces;

    /**
     * Constructor for Free Places Criteria.
     * @param nrOfPlaces minimum number of places as integer.
     */
    public FreePlacesCriteria(int nrOfPlaces){
        this.nrOfPlaces = nrOfPlaces;
    }

    /**
     *Method that checks which events meet the criteria. If event does not meet criteria, it is removed from the list.
     * @param events Observable list of event.
     * @return Observable list of event that meet this criteria.
     */
    @Override
    public ObservableList<Events> meetCriteria(ObservableList<Events> events) {
        if(nrOfPlaces == 0){return events;}
        Predicate<Events> predicate = event-> event.getAvailablePlaces() >= nrOfPlaces;

        return FXCollections.observableList(events.stream()
                .filter( predicate )
                .collect(Collectors.<Events>toList()));
    }
}
