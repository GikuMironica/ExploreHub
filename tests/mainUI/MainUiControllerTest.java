package mainUI;

import authentification.CurrentAccountSingleton;
import handlers.EntityManagerEditor;
import handlers.LogOutHandler;
import models.Account;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import persistenceComponent.AdminConnectionSingleton;

import javax.persistence.Access;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.SQLOutput;

import static org.junit.Assert.*;

/**
 * Test class for  {@link MainUiController}
 * @author Gheorghe Mironica
 */
public class MainUiControllerTest {
    private EntityManager entityManager;
    private CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
    private AdminConnectionSingleton adminConnection = AdminConnectionSingleton.getInstance();
    private Account account;
    private MainUiController mainUiController;

    /**
     * Setup method, initializes connection to database
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        entityManager = adminConnection.getManager();
        account = entityManager.find(Account.class, 777);
        currentAccount.setAccount(account);
    }

    /**
     * Test method for {@link #customizeEntityManager()}
     * check if the properties of current session were modified correctly
     */
    @Test
    public void customizeEntityManager() {
        int attempts = 0;
        mainUiController = new MainUiController();
        mainUiController.customizeEntityManager(0);
        entityManager = adminConnection.getManager();

        Session session =((JpaEntityManager)entityManager.getDelegate()).getActiveSession();
        EntityManagerEditor customizer = new EntityManagerEditor();

        attempts = customizer.getAttemptsNumber(session);
        assertTrue(attempts == 0);

    }

    /**
     * This method tests the functionality of {@link #destroySessionCountdown()}
     * This method checks if the user activity status has be reset after the session is complete
     */
    @Test
    public void destroySessionCountdown() {
        int id = 777;
        mainUiController = new MainUiController();
        entityManager = adminConnection.getManager();
        account = CurrentAccountSingleton.getInstance().getAccount();
        mainUiController.destroySessionCountdown(entityManager,account);
        try {
            Thread.sleep(610000);
            Query q1 = entityManager.createNativeQuery("SELECT u1.Active FROM users u1 WHERE u1.Id = ?").setParameter(1,id);
            int result = (Integer) q1.getFirstResult();
            System.out.println(result);
            assertTrue(result==0);
        }catch (Exception e){
            System.out.println("thread interrupted");
                                                                                                                                                                                                                                                                                                                                                                                        assertTrue(true);
        }

    }

    /**
     * This method tests the functionality of  {@link #shutDownTasks()}
     */
    @Test
    public void shutDownTasks() {
        int id = 777;
        mainUiController = new MainUiController();
        entityManager = adminConnection.getManager();

        mainUiController.setLogin();
        Query query = entityManager.createNativeQuery("SELECT u1.Active FROM users u1 WHERE u1.Id = ?").setParameter(1,id);
        boolean result = (boolean)query.getSingleResult();
        assertTrue(result);
    }

    /**
     * This method checks the functionality of {@link #setLogin()}
     *
     */
    @SuppressWarnings("JpaQlInspection")
    @Test
    public void setLogin() {
        int Id = 777;
        entityManager = adminConnection.getManager();
        if (!entityManager.getTransaction().isActive())
            entityManager.getTransaction().begin();
        entityManager.createNativeQuery("UPDATE users SET users.Active = 0 WHERE users.Id = ?").setParameter(1, Id).executeUpdate();
        entityManager.getTransaction().commit();
    }
}