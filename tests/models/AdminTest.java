package models;

import authentification.AdminConnectionSingleton;
import authentification.UserConnectionSingleton;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


public class AdminTest {
    private AdminConnectionSingleton con = AdminConnectionSingleton.getInstance();
    private EntityManager em = con.getManager();
    private Admin a1;

    @Test
    public void getId() {
        a1 = em.find(Admin.class, 101);
        a1.getId();
    }

    @Test
    public void getEmail() {
        a1 = em.find(Admin.class, 101);
        a1.getEmail();
    }

    @Test
    public void getFirstname() {
        a1 = em.find(Admin.class, 101);
        a1.getFirstname();

    }

    @Test
    public void setFirstname() {
        a1 = em.find(Admin.class, 101);
        a1.setPassword("hahahalol");
        em.getTransaction().begin();
        em.persist(a1);
        em.getTransaction().commit();
    }

    @Test
    public void getAccess() {
        a1 = em.find(Admin.class, 101);
        a1.getAccess();
    }

    @Test
    public void setAccess() {
        a1 = em.find(Admin.class, 101);
        a1.getAccess();
        a1.setAccess(1);
        em.getTransaction().begin();
        em.persist(a1);
        em.getTransaction().commit();
    }

    @Test
    public void getConnection() {
        AdminConnectionSingleton con = AdminConnectionSingleton.getInstance();
        EntityManager em = con.getManager();
    }
}