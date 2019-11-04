package listComponent;

import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

public class EventListSingletonTest {
    private UserConnectionSingleton con;
    EntityManager entityManager;
    List<Events> tempList;
    ObservableList<Events> eventsObservableList;


    @Test
    public void getEventsObservableList() {
      EventListSingleton ev = EventListSingleton.getInstance();
      eventsObservableList = ev.getEventsObservableList();
      assertTrue(eventsObservableList.size() ==8);
    }
}