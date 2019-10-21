package sample;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UserConnectionSingleton {
    private static EntityManagerFactory factory;
    private static UserConnectionSingleton ourInstance = new UserConnectionSingleton();
    private static EntityManager em;

    public static UserConnectionSingleton getInstance() {
        return ourInstance;
    }

    private UserConnectionSingleton() {

        // get connection to db using JPA Eclipse Link
        final String PERSISTENCE_UNIT_NAME = "user";
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();

    }
    // fetch ze manager
    public EntityManager getManager(){
        return em;
    }
}
