package authentification;

import models.Admin;
import models.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.Assert.*;

public class AdminStrategyTest {

    private EntityManager entityManager;
    private AdminConnectionSingleton con;

    @Before
    public void setUp() throws Exception {
        con = AdminConnectionSingleton.getInstance();
        entityManager = con.getManager();
    }

    @Test
    public void getAccount() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Admin> tq2 = entityManager.createNamedQuery(
                "Admin.findAdminByEmailPass",
                Admin.class);
        tq2.setParameter("email", "Thomas@hs-ulm.de");
        tq2.setParameter("password", "admin2");
        assertTrue(tq2.getSingleResult().getId()==102);
    }
}