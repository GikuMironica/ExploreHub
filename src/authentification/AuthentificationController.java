package authentification;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class AuthentificationController {

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button registerButton;

    private EntityManager em;

    public void init(){
        usernameField.setPromptText("Email address");
        passwordField.setPromptText("Password");
    }

        // process the credentials
    @FXML
    private void login(){
            // TEST
        UserConnectionSingleton con = UserConnectionSingleton.getInstance();
        em = con.getManager();
        AdminConnectionSingleton con2 = AdminConnectionSingleton.getInstance();
        em = con2.getManager();
            // execute NamedQuery from user class to find if password matches username
        // TypedQuery<User> tq1 = em.createNamedQuery(
        //       "User.fetchUserEmail,Password",
        //        User.class );
        // tq1.setParameter("Email", usernameField.getText());
        // tq1.setParameter("Password", passwordField.getText());
        // id = tq1.getSingleResult();
        /**
         * if password matches username then proceed to checking the Access Level, initialize an User/Admin account
         *
         */
        //  Fetch the user by PrimaryKey if was found ( ID )
        //  Admin/Customer c1 = em.find(User.class, id);


    }
        // Jump to new window, handle register process
    @FXML
    private void register(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/authentification/register.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
        // Jump to new window, proceed with recovery process
    @FXML
    private void recover(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/authentification/recover.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
