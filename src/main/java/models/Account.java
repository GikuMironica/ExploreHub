package models;

import javax.persistence.EntityManager;
/**
 * Interface for the {@link User} , {@link Admin}
 *
 * @author Gheorghe Mironica
 * */
public interface Account {
    EntityManager getConnection();
    int getId();
    int getAccess();
    String getEmail();
    String getFirstname();
    String getLastname();
    String getPassword();
    void setEmail(String email);
    void setFirstname(String firstname);
    void setLastname(String lastname);
    void setPassword(String password);

}
