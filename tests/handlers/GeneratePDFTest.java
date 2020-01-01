package handlers;

import persistenceComponent.UserConnectionSingleton;
import com.itextpdf.text.Document;
import models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class which tests the functionality of the {@link GeneratePDF} class
 *
 * @author Gheorghe Mironica
 */
public class GeneratePDFTest {
    private Document document;
    private String filename;
    private User account;
    private Transactions transactions;
    private EntityManager entityManager;
    private Events oneEvent;
    private String path;
    private GeneratePDF generatePDF;

    /**
     * Setup method which is mocking a User, Transaction, generates blank PDF,
     * Tests the pdf generation process
     * @throws Exception {@link java.io.IOException} IO exception
     */
    @Before
    @SuppressWarnings("JpaQueryApiInspection")
    public void setUp() throws Exception {
        LocalDate today = LocalDate.now();
        Date date = Date.valueOf(today);

        // temporal user
        Courses c1 = new Courses(5, "None");
        account = new User("temp", "test", "test", "test", c1);
        entityManager = UserConnectionSingleton.getInstance().getManager();

        // temporal transaction of current user
        List<Events> events = entityManager.createNamedQuery("Events.findAllEvents").getResultList();
        oneEvent = events.get(1);
        transactions = new Transactions(date, 1, 1, oneEvent, account);

        // assign the temporal transaction to user
        List<Transactions> transactionsList = new ArrayList<>();
        transactionsList.add(transactions);
        account.setTransactions(transactionsList);

        // generate pdf
        generatePDF = new GeneratePDF(account, transactions);
        document = new Document();
        document.open();
        path = generatePDF.getFilename();
        assertTrue(!generatePDF.getFilename().isBlank());
    }

    /**
     * Test method which validates the addItem method functionality,
     * this method checks that addItem works properly and adds the booked event details
     * in the document
     */
    @Test
    public void addItem() {
        try {
            generatePDF.addItem(document, transactions);
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test method which validates the addCustomer method functionality,
     * this method checks that addCustomer works properly and adds the Customer details
     * in the document
     */
    @Test
    public void addCustomer() {
        try{
            generatePDF.addCustomer(document, account);
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test method which validates the addCompanyName method functionality,
     * this method checks that addCompanyName works properly and adds the Company details
     * in the document
     */
    @Test
    public void addCompanyName() {
        try{
            generatePDF.addCompanyName(document, transactions.getEvent().getCompany());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    /**
     * This method checks the functionality of the tested method, makes sure it returns the name
     * of the newly generated pdf
     */
    @Test
    public void getFilename() {
        assertTrue(!generatePDF.getFilename().isBlank());
    }

    /**
     * This method deletes the newly generated pdf
     * @throws Exception {@link java.io.IOException} IO exception
     */
    @After
    public void tearDown() throws Exception {
        Path fileToDeletePath = Paths.get(path);
        Files.delete(fileToDeletePath);
        document.close();
    }
}