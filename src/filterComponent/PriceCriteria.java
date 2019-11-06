package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *Class that implements the Price criteria for filter.
 * @author Aleksejs Marmiss
 *
 */
public class PriceCriteria implements Criteria {

    private double price;

    /**
     * Constructor for Price Criteria.
     * @param price price boundary.
     */
    public PriceCriteria(double price){
        this.price = price;
    }

    /**
     *Method that checks which events meet the criteria. If event does not meet criteria, it is removed from the list.
     * @param events Observable list of event.
     * @return Observable list of event that meet this criteria.
     */
    @Override
    public ObservableList<Events> meetCriteria(ObservableList<Events> events) {
        Predicate<Events> predicate = event-> event.getPrice() < price;

        return FXCollections.observableList(events.stream()
                .filter( predicate )
                .collect(Collectors.<Events>toList()));
    }
}
