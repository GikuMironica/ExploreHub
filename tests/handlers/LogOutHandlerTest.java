package handlers;

import authentification.loginProcess.CurrentAccountSingleton;
import models.Account;
import org.junit.Before;
import org.junit.Test;
import persistenceComponent.AdminConnectionSingleton;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class for the {@link LogOutHandler}
 * @author Gheorghe Mironica
 */
public class LogOutHandlerTest {
    private EntityManager entityManager;
    private Account account;
    private CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
    private LogOutHandler testHandler;

    /**
     * Setup method, initializes a connection to database,
     * mocks some objects
     * @throws Exception {@link Exception} connection issues
     */
    @Before
    public void setUp() throws Exception {
        entityManager = AdminConnectionSingleton.getInstance().getManager();
        account = entityManager.find(Account.class, 777);
        currentAccount.setAccount(account);
    }

    /**
     * This method tests the functionality of {@link #handleLogOutProcess()} method.
     * When User logs out, Database event is supposed to be deleted, and activity status reset.
     */
    @Test
    public void handleLogOutProcess() {
        testHandler = new LogOutHandler(account);
        testHandler.handleLogOutProcess(false);
        Account localAccount = testHandler.getAccount();

        boolean isConnected = testHandler.getEntityManager().isOpen();
        assertNull(localAccount);
        assertFalse(isConnected);
    }

    /**
     * This method test the functionality of {@link #closeConnection()} method
     * When user logs out, connection to database is suppose to be closed.
     * Environment variables reset.
     */
    @Test
    public void closeConnection() {
        testHandler = new LogOutHandler(account);
        testHandler.handleLogOutProcess(false);
        EntityManager localManager = testHandler.getEntityManager();
        assertFalse(entityManager.isOpen());
    }

}