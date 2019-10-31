package authentification;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Admin;
import models.Events;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.List;


public class AuthentificationController {

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button registerButton, loginButton;

    private EntityManager em;

    public void init(){
        usernameField.setPromptText("Email address");
        passwordField.setPromptText("Password");
        loginButton.setDisable(true);

    }

        // process the credentials
    @FXML
    private void login(Event event) throws IOException{
        // TEST CONNECTION
        UserConnectionSingleton con = UserConnectionSingleton.getInstance();
        em = con.getManager();
        AdminConnectionSingleton con2 = AdminConnectionSingleton.getInstance();
        em = con2.getManager();
        User u1 = em.find(User.class, 6);
        System.out.println(u1.getFirstname() + " " + u1.getLastname() + " " + u1.getAccess() + " " + u1.getId() + " " + u1.getPassword());

        //TEST ENTITY RELATIONS
        if (u1.getEvents().equals(null))
            System.out.println("null events");
        else {
            List<Events> ev = u1.getEvents();
            System.out.println("Found " + ev.size() + " Events for this user");
        }
        if (u1.getCourse().equals(null))
            System.out.println("Course Null");
        else
            System.out.println(u1.getCourse().getName());
        if (u1.getTransactions().equals(null))
            System.out.println("Transactions Null");
        else {
            List<Transactions> tr = u1.getTransactions();
            System.out.println("Found " + tr.size() + " Transactions for this user");
        }

        // TEST NAMEQUERY FIND USER BY EMAIL
        TypedQuery<User> tq1 = em.createNamedQuery(
                "User.findUserbyEmail",
                User.class);
        tq1.setParameter("email", "Bredesen@hs-ulm.de");
        User u2 = tq1.getSingleResult();
        System.out.println(u2.getId() + " " + u2.getFirstname() + " " + u2.getLastname());

        // TEST NAMEDQUERY FIND USER BY GIVEN EMAIL+PASSWORD
        TypedQuery<User> tq2 = em.createNamedQuery(
                "User.findUserbyEmailPass",
                User.class);
        tq2.setParameter("email", "Giles@hs-ulm.de");
        tq2.setParameter("password", "user8");
        User u3 = tq2.getSingleResult();
        System.out.println(u3.getId() + " " + u3.getFirstname() + " " + u3.getLastname());

        // TEST NAMEDQUERY TO FIND ALL ADMINS
        TypedQuery<Admin> tq3 = em.createNamedQuery(
                "Admin.findAdmins",
                Admin.class);
        tq3.setParameter("access", 1);
        List<Admin> al = tq3.getResultList();
        for (Admin temp : al) {
            System.out.println(temp.getEmail());
        }


        Parent root = FXMLLoader.load(getClass().getResource("/mainUI/MainUI.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();


    }


    @FXML
    public void handleKeyRelease(){
        //check if both input fields are not empty, then proceed to login
        String text = usernameField.getText();
        String password = passwordField.getText();
        boolean hasText = text.isEmpty() || text.trim().isEmpty();
        boolean hasPassword = password.isEmpty() || password.trim().isEmpty();
        if(hasPassword && hasText) {
            loginButton.setDisable(hasText);
        } else if(!hasPassword && !hasText){
            loginButton.setDisable(false);
        } else if(hasPassword && !hasText){
            loginButton.setDisable(true);
        } else if(!hasPassword && hasText){
            loginButton.setDisable(true);
        }
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
