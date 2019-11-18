package tests.authentification;

import authentification.UserConnectionSingleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.jfr.Event;
import models.User;
import javax.persistence.EntityManager;
import org.junit.Test;

import javax.persistence.TypedQuery;

import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for the {@link authentification.AuthentificationController}
 *
 * @author Gheorghe Mironica
 */
public class AuthentificationControllerTest {

   private TextField usernameField;
   private TextField passwordField;
   private EntityManager em;

    @Test
    public void init() {
    }

    /**
     * Test the functionality of the login method
     *
     * @param e
     * @throws IOException
     */
    @Test
    public void login(Event e) throws IOException {
        try {
            UserConnectionSingleton con = UserConnectionSingleton.getInstance();
            em = con.getManager();

            String username = usernameField.getText();
            String password = passwordField.getText();
            TypedQuery<User> tq2 = em.createNamedQuery(
                    "User.findUserbyEmailPass",
                    User.class);
            tq2.setParameter("email", username);
            tq2.setParameter("password", password);
            User u3 = tq2.getSingleResult();

            //if user is logged in successfully, open the Home page


        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}