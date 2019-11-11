package authentification;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton Class which servers as an interface to the Database with Administrator rights,
 * @Author: Gheorghe Mironica
 */
public class AdminConnectionSingleton {
    private static EntityManagerFactory factory;
    private static AdminConnectionSingleton ourInstance = new AdminConnectionSingleton();
    private static EntityManager em;
    private final String PERSISTENCE_UNIT_NAME = "Administrator";


    /**
     * Method that returns an instance to this class
     */
    public static AdminConnectionSingleton getInstance() {
        return ourInstance;
    }
        // private c-tor
    private AdminConnectionSingleton() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();


    }
        // fetch ze manager
    /**
     * Method that returns an instance to associated with a persistence context. It is used to create, remove, update, find entities.
     */
    public EntityManager getManager(){
        return em;
    }
}
