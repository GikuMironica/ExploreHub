package authentification;

import models.User;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.Assert.*;

public class UserStrategyTest {

    private EntityManager entityManager;
    private UserConnectionSingleton con;

    @Before
    public void setUp() throws Exception {
        con = UserConnectionSingleton.getInstance();
        entityManager = con.getManager();
    }

    @Test
    public void getAccount() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<User> tq2 = entityManager.createNamedQuery(
                "User.findUserbyEmailPass",
                User.class);
        tq2.setParameter("email", "Folta@hs-ulm.de");
        tq2.setParameter("password", "user9");
        assertTrue(tq2.getSingleResult().getId()==9);
    }
}