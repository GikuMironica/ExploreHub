package listComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import filterComponent.FilterSingleton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.CompanyExcursion;
import models.Events;
import models.Excursion;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

/**
 * Singleton Class which holds the ObservableList for the main ListView
 * @author Gheorghe Mironica
 */
public class EventListSingleton {
    private static EventListSingleton ourInstance = new EventListSingleton();
    private static EntityManager entityManager;
    private static List<Events> tempList;
    private static String NATIVE_QUERY="SELECT * FROM event;";
    private static ObservableList<Events> eventsObservableList;

    public static EventListSingleton getInstance() {
        return ourInstance;
    }

    @SuppressWarnings("JpaQueryApiInspection")
    private EventListSingleton() {
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
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
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
        Thread thread = new Thread(() -> {
            List<CompanyExcursion> lc = entityManager.createNamedQuery("CompanyExcursion.findAllCExcursions", CompanyExcursion.class).getResultList();
            List<Excursion> le = entityManager.createNamedQuery("Excursion.findAllExcursions", Excursion.class).getResultList();
            tempList.clear();
            tempList.addAll(lc);
            tempList.addAll(le);
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
