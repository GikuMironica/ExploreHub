package controlPanelComponent;

import persistenceComponent.UserConnectionSingleton;
import com.github.javafaker.Faker;
import models.Account;
import models.Courses;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class which tests the functionality of the {@link ManageAdminsTabController} controller
 *
 * @author Gheorghe Mironica
 */
public class ManageAdminsTabControllerTest {

    private ManageAdminsTabController adminsTabController;
    private Account account;
    private EntityManager entityManager;
    private String NAME_PATTERN = "^[a-zA-Z]*$";
    private String EMAIL_PATTERN = "[a-zA-Z0-9._]+@mail.hs-ulm\\.(de)$";
    private String EMAIL = "hochchule@mail.hs-ulm.de";

    /**
     * Setup method, instantiates connection to DB, creates temporal user for testing
     * @throws Exception {@link Exception} Internet connection exception
     */
    @Before
    public void setUp() throws Exception {
        adminsTabController = new ManageAdminsTabController();
        Courses c1 = new Courses(5, "None");
        account = new User("temp", "test", "test", "test", c1);
        entityManager = UserConnectionSingleton.getInstance().getManager();
    }

    /**
     * Test method which tests the functionality of the fetchCourse method
     */
    @Test
    @SuppressWarnings("JpaQueryApiInspection")
    public void fetchCourse() {
        boolean ok;
        Courses tempCourse = new Courses(5,"None");
        Courses noneCourse = entityManager.createNamedQuery("Courses.findCourseByName",
                Courses.class).setParameter("name", "None").getSingleResult();
        ok = tempCourse.equals(noneCourse);
        assertTrue(ok);
    }

    /**
     * Test method which tests the functionality of the areFieldsValid method
     * It generates random names and tests the functionality of the patterns.
     */
    @Test
    public void areFieldsValid() {
        Faker faker = new Faker();

        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = EMAIL;

        boolean validFirstName = (!(firstname.isEmpty())&&(firstname.matches(NAME_PATTERN)));
        boolean validLastName = (!(lastname.isEmpty())&&(lastname.matches(NAME_PATTERN)));
        boolean validEmail = (!(email.isEmpty())&&(email.matches(EMAIL_PATTERN)));

        assertTrue(validFirstName && validLastName && validEmail);
    }

    /**
     * Test method for the generateString method,
     * It checks, validates the method which is suppose to generate unique string,
     * using the PC UUID
     */
    @Test
    public void generateString() {
        String rand = adminsTabController.generateString();
        assertTrue(!rand.isBlank());
    }

    /**
     * Method which closes the connection with database and deletes all
     * temporal entities which were created for testing purpose
     * after all methods were tested
     * @throws Exception {@link Exception}Internet connection exception
     */
    @After
    public void tearDown() throws Exception {
        if(entityManager.getTransaction().isActive()) {
            entityManager.remove(account);
        } else{
            entityManager.getTransaction().begin();
            entityManager.remove(account);
        }
        entityManager.getTransaction().commit();
    }

}