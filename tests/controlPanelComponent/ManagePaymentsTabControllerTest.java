package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Account;
import models.Transactions;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.Assert.*;

public class ManagePaymentsTabControllerTest {

    private EntityManager entityManager;
    private Account currentUser;
    private ObservableList<Transactions> transactionsList;

    /**
     * Instantiate connection to database
     * @throws Exception DB connection exception
     */
    @Before
    public void setUp() throws Exception {
        entityManager = UserConnectionSingleton.getInstance().getManager();
        assertTrue(entityManager!=null);
    }

    /**
     * Controller initialization test
     */
    @Test
    public void initialize() {
        transactionsList = FXCollections.observableArrayList();
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Transactions> tQuery = entityManager.createNamedQuery("Transactions.findAllTransactions", Transactions.class);
        transactionsList.clear();
        transactionsList.addAll(tQuery.getResultList());
        assertTrue(!transactionsList.isEmpty());
    }

}