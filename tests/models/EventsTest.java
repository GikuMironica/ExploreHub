package models;

import authentification.AdminConnectionSingleton;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the model {@link Events}
 *
 * @author Gheorghe Mironica
 */
public class EventsTest {

    private EntityManager em;
    private AdminConnectionSingleton con;

    /**
     * Establish database connection before starting
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        con = AdminConnectionSingleton.getInstance();
        em = con.getManager();
    }

    /**
     * Test the functionality of the {@link #testNamedQuery()}
     */
    @Test
    public void testNamedQuery(){
        TypedQuery<Events> tq1 = em.createNamedQuery("Events.findAllEvents", Events.class);
        List<Events> listEvents = tq1.getResultList();
        assertTrue(!listEvents.isEmpty());
    }


}