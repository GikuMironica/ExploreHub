package models;

import authentification.UserConnectionSingleton;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class for the User Model {@link User}
 *
 * @author Gheorghe Mironica
 */
public class UserTest {
    private UserConnectionSingleton con = UserConnectionSingleton.getInstance();
    private EntityManager em = con.getManager();
    private User u1;

    /**
     * Test the functionality of the {@link #getId()}
     */
    @Test
    public void getId() {
        u1 = em.find(User.class, 50);
    }

    /**
     * Test the functionality of the {@link #getEmail()}
     */
    @Test
    public void getEmail() {
        u1 = em.find(User.class, 50);
        u1.getEmail();
    }

    /**
     * Test the functionality of the {@link #setEmail()}  }
     */
    @Test
    public void setEmail() {
        u1 = em.find(User.class, 50);
        u1.setEmail("waseapartian@hs-ulm.de");
        u1.setEmail("williamsomething@hs-ulm.de");
        em.getTransaction().begin();
        em.persist(u1);
        em.getTransaction().commit();
    }

    /**
     * Test the functionality of the {@link #getFirstname()}
     */
    @Test
    public void getFirstname() {
        u1 = em.find(User.class, 50);
        u1.getFirstname();
    }

    /**
     * Test the functionality of the {@link #getLastname()}
     */
    @Test
    public void getLastname() {
        u1 = em.find(User.class, 50);
        u1.getLastname();
    }

    /**
     * Test the functionality of the {@link #getAccess()}
     */
    @Test
    public void getAccess() {
        u1 = em.find(User.class, 50);
        u1.getAccess();
    }

    /**
     * Test the functionality of the {@link #getPassword()}
     */
    @Test
    public void getPassword() {
        u1 = em.find(User.class, 50);
        u1.getPassword();
    }

    /**
     * Test the functionality of the {@link #getCourse()}
     */
    @Test
    public void getCourse() {
        u1 = em.find(User.class, 50);
        u1.getCourse();
    }


    /**
     * Test the functionality of the {@link #getConnection()}
     */
    @Test
    public void getConnection() {
        UserConnectionSingleton u1 = UserConnectionSingleton.getInstance();
        u1.getManager();
    }
}