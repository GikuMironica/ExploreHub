package handlers;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.DatabaseLogin;

public class EntityManagerEditor implements SessionCustomizer {

    public void customize(Session session) {
        DatabaseLogin login = (DatabaseLogin)session.getDatasourceLogin();
        login.setQueryRetryAttemptCount(0);
    }

}