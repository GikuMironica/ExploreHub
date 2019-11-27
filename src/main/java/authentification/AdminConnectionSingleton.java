package authentification;

import handlers.Convenience;
import javafx.scene.control.Alert;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Singleton Class which servers as an interface to the Database with Administrator rights,
 *
 * @author: Gheorghe Mironica
 */
public class AdminConnectionSingleton {
    private static EntityManagerFactory factory;
    private static AdminConnectionSingleton ourInstance = null;
    private static EntityManager em;
    private final String PERSISTENCE_UNIT_NAME = "Administrator";


    /**
     * Method that returns an instance to this class
     */
    public static AdminConnectionSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new AdminConnectionSingleton();
        }
        return ourInstance;
    }
        // private c-tor
    private AdminConnectionSingleton() {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            em = factory.createEntityManager();
        }catch(Exception e){
            Convenience.showAlert(Alert.AlertType.INFORMATION, "Internet Connection", "Oops, looks like you have no internet connection","Try later.");
            return;
        }

    }
        // fetch ze manager
    /**
     * Method that returns an instance to associated with a persistence context. It is used to create, remove, update, find entities.
     */
    public EntityManager getManager(){
        return em;
    }

    /**
     * Method which closes this connection
     */
    public void closeConnection(){
        em.close();
        em = null;
        ourInstance = null;
    }
}
