package authentification;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Courses;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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