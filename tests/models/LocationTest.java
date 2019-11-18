package models;

import authentification.UserConnectionSingleton;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class for the Location Model {@link Location}
 *
 * @author Gheorghe Mironica
 */
public class LocationTest {
    private UserConnectionSingleton con;
    private EntityManager entityManager;
    Events event;
    Location location;

    /**
     * Establish database connection before starting
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        con = UserConnectionSingleton.getInstance();
        entityManager = con.getManager();
        event = entityManager.find(Events.class, 1);
        location = entityManager.find(Location.class, event.getId());
    }

    /**
     * Test the functionality of the {@link #getLatitude()}
     */
    @Test
    public void getLatitude() {
        assertTrue(location.getLatitude() == 48.4192186);
    }

    /**
     * Test the functionality of the {@link #getLongitude()}
     */
    @Test
    public void getLongitude() {
        assertTrue(location.getLongitude() == 9.9323005);
    }
}