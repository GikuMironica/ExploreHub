package sample;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AdminConnectionSingleton {
    private static EntityManagerFactory factory;
    private static AdminConnectionSingleton ourInstance = new AdminConnectionSingleton();
    private static EntityManager em;

        // ensure single global access via whole system
    public static AdminConnectionSingleton getInstance() {
        return ourInstance;
    }
        // private c-tor
    private AdminConnectionSingleton() {

            // get connection to db using JPA Eclipse Link
        final String PERSISTENCE_UNIT_NAME = "admin";
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();


    }
        // fetch ze manager
    public EntityManager getManager(){
        return em;
    }
}
