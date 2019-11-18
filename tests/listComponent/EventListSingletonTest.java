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

/**
 * This test class tests the functionality of the {@link EventListSingleton}
 *
 * @author Gheorghe Mironica
 */
public class EventListSingletonTest {
    private EntityManager entityManager;
    private List<Events> tempList;
    private ObservableList<Events> eventsObservableList;

    /**
     * This method should test the connection to database
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        entityManager = UserConnectionSingleton.getInstance().getManager();
        assertTrue(!(entityManager==null));
    }

    /**
     * This method tests the functionality of the {@link #getEventsObservableList()}
     *
     */
    @Test
    public void getEventsObservableList() {

      EventListSingleton ev = EventListSingleton.getInstance();
      eventsObservableList = ev.getEventsObservableList();
      assertTrue(!eventsObservableList.isEmpty());

    }
}