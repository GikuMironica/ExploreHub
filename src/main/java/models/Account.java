package models;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for the {@link User} , {@link Admin}
 *
 * @author Gheorghe Mironica
 * */
public interface Account {

    int getId();

    /**
     *  Method to get the entity email
     */
    String getEmail();

    /**
     *  Method to reset the entity email
     */
    void setEmail(String email);

    /**
     *  Method to get the entity First Name
     */
    String getFirstname();

    /**
     *  Method to reset the entity's First Name
     */
    void setFirstname(String firstname);

    /**
     *  Method to get the entity's Last Name
     */
    String getLastname();

    /**
     *  Method to reset the entity's Last Name
     */
    void setLastname(String lastname);

    /**
     *  Method to get the entity's access level
     */
    int getAccess();

    /**
     *  Method to get the entity's password
     */
    String getPassword();

    /**
     *  Method to reset the entity's password
     */
    void setPassword(String password);

    /**
     *  Method to get the entity associated attribute
     * @return Course object if found
     */
    Courses getCourse();

    /**
     *  Method to set an entity's associated attributes
     * @param course Course object
     */
    void setCourse(Courses course);

    /**
     *  Method to set an entity's associated attributes
     * @return List of Transactions
     */
    List<Transactions> getTransactions();

    /**
     *  Method to associate this entity with a list of transactions
     * @param transactions List of transactions
     */
    void setTransactions(List<models.Transactions> transactions);

    /**
     *  Method to set an entity's associated attributes
     * @return  List of Event objects
     */
    List<Events> getEvents();

    /**
     *  Method to associate entity with a list of event objects
     * @param events List type events
     */
    void setEvents(List<Events> events);

    List<models.Events> getBookedEvents();

    void setBookedEvents(List<models.Events> bookedEvents);

    /**
     *  Method to access the access the database interface as User
     * @return Entity Manager
     */
    EntityManager getConnection();

    void closeConnection();
}
