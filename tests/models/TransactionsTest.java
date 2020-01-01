package models;

import persistenceComponent.UserConnectionSingleton;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for the Transactions Model,
 * Mainly, it tests the named queries
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
public class TransactionsTest {
    private List<Transactions> transactions;
    private EntityManager entityManager;
    private List<Events> eventsList;
    private List<Account> allUsers;
    private final int criteria = 2;

    /**
     * Setup Method, initialized connection with DB
     * @throws Exception {@link Exception} Internet connection problems
     */
    @Before
    public void setUp() throws Exception {
        UserConnectionSingleton userConnectionSingleton = UserConnectionSingleton.getInstance();
        entityManager = userConnectionSingleton.getManager();
        allUsers = new ArrayList<>();
        allUsers.addAll(entityManager.createNamedQuery("User.findAllUser").getResultList());
    }

    /**
     * Test Method for Transactions.findAllActiveTransactions named query
     */
    @Test
    public void findAllActiveTransactions() {
        transactions = new ArrayList<>();
        TypedQuery<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAllActiveTransactions", Transactions.class);
        transactions.addAll(transactionList.getResultList());
        assertFalse(transactions == null);
    }

    /**
     * Test Method for Transactions.findAllProcessedTransactions named query
     */
    @Test
    public void findAllProcessedTransactions() {
        transactions = new ArrayList<>();
        TypedQuery<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAllProcessedTransactions", Transactions.class);
        transactions.addAll(transactionList.getResultList());
        assertFalse(transactions == null);
    }

    /**
     * Test Method for Transactions.findAllTransactions named query
     */
    @Test
    public void findAllTransactions() {
        transactions = new ArrayList<>();
        TypedQuery<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAllTransactions", Transactions.class);
        transactions.addAll(transactionList.getResultList());
        assertFalse(transactions == null);
    }

    /**
     * Test Method for Transactions.findAllOngoingAccepted named query
     */
    @Test
    public void findAllOngoingAccepted() {
        eventsList = entityManager.createNamedQuery("Events.findAllEvents", Events.class).getResultList();
        transactions = new ArrayList<>();
        int counter = 0;
        for(Account akk : allUsers){
            for(Events e : eventsList){
                TypedQuery<Transactions> transactionList = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
                transactionList.setParameter("userId",akk.getId());
                transactionList.setParameter("id",e.getId());
                transactions.addAll(transactionList.getResultList());
                if(!transactions.isEmpty()){
                    counter++;
                }
            }
        }
        if(counter<criteria){
            fail();
        }
    }

}