package authentification;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Account;
import models.Admin;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;


public class AuthentificationController {

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label alert;
    private EntityManager entityManager;

    public void init() throws IOException{
        usernameField.setPromptText("Email address");
        passwordField.setPromptText("Password");
        loginButton.setDisable(true);
        alert.setVisible(false);
    }

    @FXML
    private void login(Event event) throws IOException{
        String username = usernameField.getText();
        String password = passwordField.getText();
        CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
        try {
            // Try to establish connection as a user
            UserConnectionSingleton con = UserConnectionSingleton.getInstance();
            entityManager = con.getManager();

            @SuppressWarnings("JpaQueryApiInspection")
            Query tq1 = entityManager.createNamedQuery(
                    "User.determineAccess",
                    User.class);
            tq1.setParameter("email", username);
            tq1.setParameter("password", password);
            int i = (int)tq1.getSingleResult();

            // create unique instance of logged in use through the system
            if(i==0) {
                @SuppressWarnings("JpaQueryApiInspection")
                TypedQuery<User> tq2 = entityManager.createNamedQuery(
                        "User.findUserbyEmailPass",
                        User.class);
                tq2.setParameter("email", username);
                tq2.setParameter("password", password);
                currentAccount.setAccount(tq2.getSingleResult());
            }
            else {
                @SuppressWarnings("JpaQueryApiInspection")
                TypedQuery<Admin> tq2 = entityManager.createNamedQuery(
                        "Admin.findAdminByEmailPass",
                        Admin.class);
                tq2.setParameter("email", username);
                tq2.setParameter("password", password);
                currentAccount.setAccount(tq2.getSingleResult());
            }
        }catch(Exception e){
            e.printStackTrace();
            alert.setText("Invalid Email or Password");
            alert.setVisible(true);
            usernameField.clear();
            passwordField.clear();
            //make the Alert appear and make it disappear after 3 seconds
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(3)
            );
            visiblePause.setOnFinished(
                    (ActionEvent ev) -> {
                        alert.setVisible(false);
                    }
            );
            visiblePause.play();
            return;
        }
        //if user is logged in successfully, open the Home pag
        Parent root = FXMLLoader.load(getClass().getResource("/mainUI/mainUI.fxml"));
        Scene scene = new Scene(root, 800, 600);
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

    @FXML
    private void register(Event event) throws IOException {
        //Check the internet connection first
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/authentification/register.fxml"));
            Scene scene = new Scene(root, 600, 400);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch(Error e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void recover(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/authentification/recover.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
