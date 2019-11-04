package models;

import authentification.AdminConnectionSingleton;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

public class EventsTest {

    private EntityManager em;
    private AdminConnectionSingleton con;

    @Before
    public void setUp() throws Exception {
        con = AdminConnectionSingleton.getInstance();
        em = con.getManager();
    }

    @Test
    public void testNamedQuery(){
        TypedQuery<Events> tq1 = em.createNamedQuery("Events.findAllEvents", Events.class);
        List<Events> listEvents = tq1.getResultList();
        assertTrue(!listEvents.isEmpty());
    }

    @Test
    public void getLocation() {
        Events e1 = em.find(Events.class, 1);
        assertTrue(e1.getLocation().getLatitude()==48.4192186);
        assertTrue(e1.getLocation().getLongitude()==9.9323005);
        assertTrue(e1.getLocation().getCity().matches("Ulm"));
    }

    @Test
    public void getPicture(){
        Events e2 = em.find(Events.class, 5);
        assertTrue(e2.getPicture().getLogo().matches("https://i.imgur.com/y02zvQU.png"));
        assertTrue(e2.getPicture().getPicture().matches("https://i.imgur.com/Bhs5aVb.jpg"));
    }

    @Test
    public void setPicture(){
        Events e3 = em.find(Events.class, 1);
        em.getTransaction().begin();
        e3.getPicture().setLogo("https://i.imgur.com/YWmWNTz.jpg");
        em.getTransaction().commit();
        assertTrue(e3.getPicture().getLogo().matches("https://i.imgur.com/YWmWNTz.jpg"));

        em.getTransaction().begin();
        e3.getPicture().setLogo("hah");
        em.getTransaction().commit();
        assertTrue(e3.getPicture().getLogo().matches("hah"));

        em.getTransaction().begin();
        e3.getPicture().setLogo("https://i.imgur.com/YWmWNTz.jpg");
        em.getTransaction().commit();
        assertTrue(e3.getPicture().getLogo().matches("https://i.imgur.com/YWmWNTz.jpg"));
    }
}