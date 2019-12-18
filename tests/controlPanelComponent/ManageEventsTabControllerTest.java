package controlPanelComponent;

import authentification.AdminConnectionSingleton;
import javafx.event.Event;
import models.Events;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import static org.junit.Assert.*;

/**
 * This test class tests the functionality of the {@link ManageEventsTabController}
 *
 * @author Gheorghe Mironica
 */
public class ManageEventsTabControllerTest {
    private EntityManager em;
    private final String LATITUDE_PATTERN="^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$";
    private final String LONGITUDE_PATTERN="^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$";
    private final String PRICE_PATTERN="^(-?)(0|([1-9][0-9]*))((.|,)[0-9][0-9]?)?$";
    private final String CITY_PATTERN="^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private final String ORGANISATION_PATTERN="^[A-Z]([a-zA-Z0-9]|[- @\\.#&!])*$";

    /**
     * Set up database connection before starting
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        em = AdminConnectionSingleton.getInstance().getManager();
    }

    /**
     * This method tests the functionality of the {@link #validateInput() }
     *
     */
    @Test
    public void validateInput() {
        assertTrue(!("123.142356").matches(LATITUDE_PATTERN));
        assertTrue(!("90.2352525").matches(LATITUDE_PATTERN));
        assertTrue(("89.142356").matches(LATITUDE_PATTERN));
        assertTrue(!("-90.23343").matches(LATITUDE_PATTERN));
        assertTrue(!("as#80.142356f").matches(LATITUDE_PATTERN));
        assertTrue(!("fasg1g23g.1g4asd235g6").matches(LATITUDE_PATTERN));
        assertTrue(!("180.2325523").matches(LONGITUDE_PATTERN));
        assertTrue(("130.235221").matches(LONGITUDE_PATTERN));
        assertTrue(("-130.241523").matches(LONGITUDE_PATTERN));
        assertTrue(!("130,134234").matches(LONGITUDE_PATTERN));
        assertTrue(!("30.23412.").matches(LONGITUDE_PATTERN));
        assertTrue(("1423.0").matches(PRICE_PATTERN));
        assertTrue(("12.99").matches(PRICE_PATTERN));
        assertTrue(("12").matches(PRICE_PATTERN));
        assertTrue(("1,99").matches(PRICE_PATTERN));
        assertTrue(!("1D2,99").matches(PRICE_PATTERN));
        assertTrue(!(".12,99").matches(PRICE_PATTERN));
        assertTrue(("hAHAH").matches(CITY_PATTERN));
        assertTrue(("Chisinau City").matches(CITY_PATTERN));
        assertTrue(("Badden-Wurttemberg").matches(CITY_PATTERN));
        assertTrue(("Lool haha haha12432").matches(ORGANISATION_PATTERN));
    }

}