package handlers;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.DatabaseLogin;

/**
 * Class which is responsible for customization of Entity Manager properties
 *
 * @author Gheorghe Mironica
 */
public class EntityManagerEditor implements SessionCustomizer {
    private int count = 0;
    /**
     * This method is responsible for customizing entity manager number of connection attempts done by Entity Manager
     * @param session {@link Session} current session
     */
    public void customize(Session session) {
        DatabaseLogin login = (DatabaseLogin)session.getDatasourceLogin();
        login.setQueryRetryAttemptCount(count);
    }

    /**
     * Method which sets the number of attempts to be done
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Method which returns the nummber of attempts set.
     * @param session {@link Session} current session
     * @return {@link Integer} result
     */
    public int getAttemptsNumber(Session session){
        DatabaseLogin login = (DatabaseLogin)session.getDatasourceLogin();
        return login.getQueryRetryAttemptCount();
    }
}