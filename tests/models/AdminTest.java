package models;

import authentification.AdminConnectionSingleton;
import authentification.UserConnectionSingleton;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the Model {@link Admin}
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
public class AdminTest {
    private AdminConnectionSingleton con;
    private EntityManager em;
    private Admin a1;

    @Before
    public void setUp(){
        con = AdminConnectionSingleton.getInstance();
        em = con.getManager();
        TypedQuery<Admin> admins  = em.createNamedQuery("Admin.findAdmins", Admin.class);
        List<Admin> adminL = new ArrayList<>();
        adminL.addAll(admins.getResultList());
        a1 = adminL.get(1);
    }

    /**
     * Test the functionality of the {@link #getId()}
     */
    @Test
    public void getId() {
        a1.getId();
    }

    /**
     * Test the functionality of the {@link #getEmail()}
     */
    @Test
    public void getEmail() {
        a1.getEmail();
    }


    /**
     * Test the functionality of the {@link #getConnection()}
     */
    @Test
    public void getConnection() {
        AdminConnectionSingleton con = AdminConnectionSingleton.getInstance();
        EntityManager em = con.getManager();
    }
}