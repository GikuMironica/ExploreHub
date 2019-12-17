package tasks;

import authentification.CurrentAccountSingleton;
import handlers.CacheSingleton;
import javafx.concurrent.Task;
import models.Account;
import models.Events;
import models.Transactions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class UpdateCacheTask extends Task {

    @Override
    protected Object call() throws Exception {
        Account currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        EntityManager entityManager = currentAccount.getConnection();

        TypedQuery<Events> query = entityManager.createNamedQuery("Events.findAllEvents", Events.class);
        List<Events> allEvents = query.getResultList();

        CacheSingleton.getInstance().updateCache(allEvents);
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

    }

    @Override
    protected void failed() {
        super.failed();
    }
}
