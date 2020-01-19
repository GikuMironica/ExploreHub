package authentification;

import persistenceComponent.UserConnectionSingleton;
import models.User;
import javax.persistence.EntityManager;
import org.junit.Test;

import javax.persistence.TypedQuery;

import java.awt.*;
import java.io.IOException;

/**
 * Test class for the {@link authentification.AuthentificationController}
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
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
     * @throws IOException exception IO
     */
    @Test
    public void login() throws IOException {
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

        } catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}