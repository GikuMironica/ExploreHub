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

    /**
     * This Method tests the functionality of the Delete Event button
     */
    @Test(expected = NoResultException.class)
    public void deleteEvent(){
        Query createEvent = em.createNativeQuery("INSERT INTO event " +
                "VALUES(1000, '2021-01-03', 'Eurotour', 60, 60, 3, 'hahahah', 'hahahahaha');");
        Query insertWishist = em.createNativeQuery("INSERT INTO wishlist" +" VALUES(20, 1000);");
        Query inertLocation = em.createNativeQuery("INSERT INTO location VALUES(1000, 13.23423, 100.2342, 'Ulm');");
        Query insertPicture = em.createNativeQuery("INSERT INTO pictures (EventID, Logo, Picture)\n" +
                "VALUES (1000 ,'https://i.imgur.com/f0qP2Jx.png', 'https://i.imgur.com/2taPhGM.png');");
        Query correctDate = em.createNativeQuery("UPDATE event\n" + "SET Date = '2020-01-29'\n" + "WHERE Id=1000;");
        Query checkEvent = em.createNativeQuery("SELECT * FROM event WHERE Id=1000");
        Query checkLocation = em.createNativeQuery("SELECT * FROM location WHERE EventID=1000");
        Query checkPicture = em.createNativeQuery("SELECT * FROM pictures WHERE EventID=1000");
        Query checkWishlist = em.createNativeQuery("SELECT * FROM wishlist WHERE EventID=1000");


        em.getTransaction().begin();
        createEvent.executeUpdate();
        insertWishist.executeUpdate();
        inertLocation.executeUpdate();
        insertPicture.executeUpdate();
        correctDate.executeUpdate();
        em.getTransaction().commit();

        Events e1 = em.find(Events.class, 1000);
        em.getTransaction().begin();
        em.remove(e1);
        em.getTransaction().commit();

        assertNull(checkEvent.getSingleResult());
        assertNull(checkLocation.getSingleResult());
        assertNull(checkPicture.getSingleResult());
        assertNull(checkWishlist.getSingleResult());

    }
}