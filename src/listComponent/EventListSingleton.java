package listComponent;

import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import models.Events;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Singleton Class which holds the ObservableList for the main ListView
 * @author Gheorghe Mironica
 */
public class EventListSingleton {
    private static EventListSingleton ourInstance = new EventListSingleton();
    private static UserConnectionSingleton con;
    private static EntityManager entityManager;
    private static List<Events> tempList;
    private ObservableList<Events> eventsObservableList;

    public static EventListSingleton getInstance() {
        return ourInstance;
    }

    private EventListSingleton() {
        con = UserConnectionSingleton.getInstance();
        entityManager = con.getManager();
        TypedQuery<Events> tq1 = entityManager.createNamedQuery("Events.findAllEvents", Events.class);
        tempList = tq1.getResultList();
        eventsObservableList = FXCollections.observableArrayList();
        eventsObservableList.addAll(tempList);
    }

    /**
     * Method to get the instance of the eventsList
     * @return return the Event List
     */
    public ObservableList<Events> getEventsObservableList(){
        return eventsObservableList;
    }

    public void setEventsObservableList(ObservableList<Events> eventsList){
        this.eventsObservableList = eventsList;
    }
}
