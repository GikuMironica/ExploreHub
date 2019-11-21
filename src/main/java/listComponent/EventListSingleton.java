package listComponent;

import authentification.UserConnectionSingleton;
import filterComponent.FilterSingleton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private static ObservableList<Events> eventsObservableList;

    public static EventListSingleton getInstance() {
        return ourInstance;
    }

    @SuppressWarnings("JpaQueryApiInspection")
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

    /**
     * Method which refreshes the ListView, executed as Background Scheduled Task
     */
    @SuppressWarnings("JpaQueryApiInspection")
    public void refreshList(){
        Thread thread = new Thread(() -> {
            System.out.println("EntityManager cache refreshed - EventListSingleton");
            TypedQuery<Events> tq1 = entityManager.createNamedQuery("Events.findAllEvents", Events.class);
            tempList = tq1.getResultList();
            Platform.runLater(() -> {
                eventsObservableList.clear();
                eventsObservableList.addAll(tempList);
            });
        });
        thread.start();
        //FilterSingleton filter = FilterSingleton.getInstance();
        //filter.filterItems();
    }
}
