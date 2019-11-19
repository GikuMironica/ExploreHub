package filterComponent;

import javafx.collections.ObservableList;
import models.Events;

/**
 *Class that implements the logic And criteria for filter.
 * @author Aleksejs Marmiss
 *
 */
public class AndCriteria implements Criteria {

    private Criteria criteria;
    private Criteria otherCriteria;

    /**
     * Constructor for AndCriteria
     * @param criteria first criteria that has to be checked.
     * @param otherCriteria second criteria that has to be checked.
     */
    public AndCriteria(Criteria criteria, Criteria otherCriteria){
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    /**
     *Method that checks which events meet the criteria. If event does not meet criteria, it is removed from the list.
     * @param events Observable list of event.
     * @return Observable list of event that meet this criteria.
     */
    @Override
    public ObservableList<Events> meetCriteria(ObservableList<Events> events) {
        ObservableList firstCriteriaEvents = criteria.meetCriteria(events);
        return otherCriteria.meetCriteria(firstCriteriaEvents);
    }
}
