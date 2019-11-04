package models;

import authentification.AdminConnectionSingleton;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

public class EventsTest {

    private AdminConnectionSingleton con = AdminConnectionSingleton.getInstance();
    private EntityManager em = con.getManager();

    @Test
    public void testNamedQuery(){
        TypedQuery<Events> tq1 = em.createNamedQuery("Events.findAllEvents", Events.class);
        List<Events> listEvents = tq1.getResultList();
        assertTrue(!listEvents.isEmpty());
       System.out.println(listEvents.size());
    }

}