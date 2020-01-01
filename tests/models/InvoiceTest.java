package models;

import persistenceComponent.UserConnectionSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Invoice model,
 * Mainly it tests the embedded named queries
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
public class InvoiceTest {
    private EntityManager entityManager;
    private List<Account> users;
    private final int criteria = 1;

    /**
     * Setup method which instantiates connection to DB
     */
    @BeforeEach
    public void setUp() {
        UserConnectionSingleton userConnectionSingleton = UserConnectionSingleton.getInstance();
        entityManager = userConnectionSingleton.getManager();

        users = new ArrayList<>();
        TypedQuery<User> userList = entityManager.createNamedQuery("User.findAllUser", User.class);
        users.addAll(userList.getResultList());
    }

    /**
     * Test method the the Invoice.findAllInvoicesForUser named query,
     * checks if this namedquery works properly, retrieves valid entities from DB
     */
    @Test
    public void findAllInvoicesForUser() {
        List<Invoice> invoices = new ArrayList<>();
        int counter = 0;
        for(Account akk : users){
           TypedQuery<Invoice> invoiceList = entityManager.createNamedQuery("Invoice.findAllInvoicesForUser", Invoice.class);
           invoiceList.setParameter("user",akk.getLastname());
           invoices.addAll(invoiceList.getResultList());
           if(!invoices.isEmpty()){
               counter++;
           }
       }
        if(counter<criteria){
            fail();
        }
    }

}