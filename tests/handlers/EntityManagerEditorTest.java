package handlers;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.Session;
import org.junit.Before;
import org.junit.Test;
import persistenceComponent.AdminConnectionSingleton;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class responsible for testing functionality of {@link EntityManagerEditor} class
 * @author Gheorghe Mironica
 */
public class EntityManagerEditorTest {
    private EntityManager entityManager;
    private AdminConnectionSingleton adminConnectionSingleton = AdminConnectionSingleton.getInstance();
    private Session session;
    private EntityManagerEditor customizer;

    /**
     * Setup method, initialize Entity Manager, mock some objects
     */
    @Before
    public void setUp(){
        entityManager = adminConnectionSingleton.getManager();
        session =((JpaEntityManager)entityManager.getDelegate()).getActiveSession();
        customizer = new EntityManagerEditor();
    }

    /**
     * Method which checks the functionality of {@link #customize()} method
     */
    @Test
    public void customize() {
        int i = 0;
        customizer.setCount(i);
        customizer.customize(session);

        int result = customizer.getAttemptsNumber(session);
        assertTrue(result==0);
    }
}