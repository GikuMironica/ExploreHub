package listComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import models.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the {@link EventWindowController}
 *
 * @author Gheorghe Mironica
 */
public class EventWindowControllerTest {

    private EntityManager entityManager;
    private Account account;

    /**
     * Setup db connection
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        entityManager = UserConnectionSingleton.getInstance().getManager();
        User u1 = entityManager.find(User.class, 6);
        CurrentAccountSingleton.getInstance().setAccount(u1);
        account = CurrentAccountSingleton.getInstance().getAccount();
        assertTrue(entityManager!=null);
        assertTrue(account!=null);
    }

    /**
     * Test if valid user
     */
    @Test
    public void checkUser() {
       assertTrue(account instanceof User);
    }

    /**
     * Test if wishlist works
     */
    @Test
    public void checkIfInWishlist() {
        boolean ok = ((User)(account)).checkEventPresence(entityManager, 1);
        Events e1 = entityManager.find(Events.class, 1);
        if(ok){
            assertTrue(((User)(account)).getEvents().contains(e1));
        } else{
            assertFalse(((User)(account)).getEvents().contains(e1));
        }
    }

    /**
     * Test the booking preconditions
     */
    @Test
    public void checkIfBooked() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Transactions> tq1 = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
        tq1.setParameter("id", 1);
        tq1.setParameter("userId", account.getId());
        int size = tq1.getResultList().size();
        List<Transactions> transactions = tq1.getResultList();
        if (size>1){
            assertTrue(!transactions.isEmpty());
        } else if (size==0){
            assertTrue(transactions.isEmpty());
        }
    }
}