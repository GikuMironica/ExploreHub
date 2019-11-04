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

        // ensure single global access via whole system
    /**
     * Method that returns an instance to this class
     */
    public static AdminConnectionSingleton getInstance() {
        return ourInstance;
    }
        // private c-tor
    private AdminConnectionSingleton() {

            // get connection to db using JPA Eclipse Link
        final String PERSISTENCE_UNIT_NAME = "Administrator";
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
