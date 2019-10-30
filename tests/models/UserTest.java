package models;

import authentification.UserConnectionSingleton;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


public class UserTest {
    private UserConnectionSingleton con = UserConnectionSingleton.getInstance();
    private EntityManager em = con.getManager();
    private User u1;

    @Test
    public void getId() {
        u1 = em.find(User.class, 50);
    }

    @Test
    public void getEmail() {
        u1 = em.find(User.class, 50);
        u1.getEmail();
    }

    @Test
    public void setEmail() {
        u1 = em.find(User.class, 50);
        u1.setEmail("waseapartian@hs-ulm.de");
        u1.setEmail("williamsomething@hs-ulm.de");
        em.getTransaction().begin();
        em.persist(u1);
        em.getTransaction().commit();
    }

    @Test
    public void getFirstname() {
        u1 = em.find(User.class, 50);
        u1.getFirstname();
    }


    @Test
    public void getLastname() {
        u1 = em.find(User.class, 50);
        u1.getLastname();
    }


    @Test
    public void getAccess() {
        u1 = em.find(User.class, 50);
        u1.getAccess();
    }

    @Test
    public void getPassword() {
        u1 = em.find(User.class, 50);
        u1.getPassword();
    }


    @Test
    public void getCourse() {
        u1 = em.find(User.class, 50);
        u1.getCourse();
    }


        // TEST CONNECTION
    @Test
    public void getConnection() {
        UserConnectionSingleton u1 = UserConnectionSingleton.getInstance();
        u1.getManager();
    }
}