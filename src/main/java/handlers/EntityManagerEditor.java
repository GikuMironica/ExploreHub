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

    /**
     * This method is responsible for customizing entity manager number of connection attempts done by Entity Manager
     * @param session {@link Session} current session
     */
    public void customize(Session session) {
        DatabaseLogin login = (DatabaseLogin)session.getDatasourceLogin();
        login.setQueryRetryAttemptCount(0);
    }

}