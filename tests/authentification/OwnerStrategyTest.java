package authentification;

import models.Admin;
import models.Owner;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.Assert.*;

public class OwnerStrategyTest {

    private EntityManager entityManager;
    private AdminConnectionSingleton con;

    /**
     * Method which connects to DataBase before starting
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        con = AdminConnectionSingleton.getInstance();
        entityManager = con.getManager();
    }

    /**
     * Test the functionality of the {@link #getAccount()}
     *
     */
    @Test
    public void getAccount() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Owner> tq2 = entityManager.createNamedQuery(
                "Owner.findOwnerByEmailPass",
                Owner.class);
        tq2.setParameter("email", "hochschule@hs-ulm.de");
        tq2.setParameter("password", "1234");
        assertTrue(tq2.getSingleResult().getId()==777);
    }


}