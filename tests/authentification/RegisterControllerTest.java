package authentification;

import models.Courses;
import org.junit.Test;
import persistenceComponent.UserConnectionSingleton;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Test class for the {@link RegisterController}
 *
 * @author Gheorghe Mironica
 */
public class RegisterControllerTest {

    /**
     * Test the functionality of the {@link #initialize()}
     */
    @Test
    public void initialize() {
        try{
            UserConnectionSingleton con = UserConnectionSingleton.getInstance();
            EntityManager entityManager = con.getManager();
            assertTrue(entityManager.find(Courses.class, 1).getName().matches("CTS"));
        }catch(Exception e){
            return;
        }
    }

}