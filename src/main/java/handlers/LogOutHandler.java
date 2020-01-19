package handlers;

import authentification.CurrentAccountSingleton;
import persistenceComponent.GuestConnectionSingleton;
import authentification.RememberUserDBSingleton;
import models.Account;

import javax.persistence.EntityManager;

/**
 * This class is responsible for handling the logging out process
 *
 * @author Gheorghe Mironica
 */
public class LogOutHandler {
    private EntityManager entityManager;
    private Account account;

    public LogOutHandler(Account account){
        this.account = account;
    }

    /**
     * This method deletes resets the application current user, shuts down connection to Database and
     * deletes the database event which is responsible for loggin out user in case of Application crash
     */
    public void handleLogOutProcess(Boolean isX){
        int Id = account.getId();
        entityManager = account.getConnection();

        // delete db event
        try {
            if (!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            entityManager.createNativeQuery("UPDATE users SET users.Active = 0 WHERE users.Id = ?").setParameter(1, Id).executeUpdate();
            entityManager.getTransaction().commit();

            if (!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            entityManager.createNativeQuery("DROP EVENT IF EXISTS session_event_" + Id + ";").executeUpdate();
            entityManager.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
           // System.out.println("logout exception");
        }

        closeConnection();
        startGuestConnection(isX);
    }

    /**
     * This method closes connection with database as User
     */
    protected void closeConnection(){
        CurrentAccountSingleton.getInstance().getAccount().closeConnection();
        account = null;
    }

    /**
     * This method instantiates a connection to Database as a Guest
     */
    protected void startGuestConnection(Boolean isX){
        GuestConnectionSingleton.getInstance();
        if(!isX) {
            RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
            userDB.cleanDB();
        }
    }

    /**
     * Method to get the current Account
     * @return {@link Account} Account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Method to get the current session
     * @return {@link EntityManager} Entity Manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
