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
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;

/**
 * Singleton Class which holds the ObservableList for the main ListView
 * @author Gheorghe Mironica
 */
public class EventListSingleton{
    private static EventListSingleton ourInstance = new EventListSingleton();
    private static EntityManager entityManager;
    private static List<Events> tempList;
    private static String NATIVE_QUERY="SELECT * FROM event;";
    private static ObservableList<Events> eventsObservableList;

    public static EventListSingleton getInstance() {
        return ourInstance;
    }

    /**
     * Private Constructor
     */
    @SuppressWarnings("JpaQueryApiInspection")
    private EventListSingleton() {
        List<Events> tempTrash = new ArrayList<Events>();
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
        TypedQuery<Events> tq1 = entityManager.createNamedQuery("Events.findAllEvents", Events.class);
        tempList = tq1.getResultList();
        for(Events e : tempList){
            if(e.getDate().before(Date.valueOf(LocalDate.now())))
                tempTrash.add(e);
        }
        tempList.removeAll(tempTrash);
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

    /**
     * setter
     * @param eventsList {@link ObservableList} input list
     */
    public void setEventsObservableList(ObservableList<Events> eventsList){
        this.eventsObservableList = eventsList;
    }

    /**
     * Method which refreshes the ListView, executed as Background Scheduled Task
     */
    @SuppressWarnings("JpaQueryApiInspection")
    public void refresh(){
                List<Events> tempTrash = new ArrayList<Events>();
                entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
                List<CompanyExcursion> lc = entityManager.createNamedQuery("CompanyExcursion.findAllCExcursions", CompanyExcursion.class).getResultList();
                List<Excursion> le = entityManager.createNamedQuery("Excursion.findAllExcursions", Excursion.class).getResultList();
                tempList.clear();
                tempList.addAll(lc);
                tempList.addAll(le);
                tempTrash.clear();

                for (Events e : tempList) {
                    if (e.getDate().before(Date.valueOf(LocalDate.now())))
                        tempTrash.add(e);
                }
                tempList.removeAll(tempTrash);
    }

    /**
     * Method which refreshes the List of events
     */
    public void refreshList() {
        Thread thread = new Thread(() -> {
            refresh();
            FilterSingleton filter = FilterSingleton.getInstance();

            Platform.runLater(()->{
                eventsObservableList.clear();
                eventsObservableList.addAll(tempList);
                filter.updateFilter();
            });
        });
        thread.start();
    }
}
