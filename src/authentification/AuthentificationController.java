package authentification;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Admin;
import models.Events;
import models.Transactions;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AuthentificationController {

    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button registerButton, loginButton;
    @FXML
    private Label alert;

    private EntityManager em;

    public void init() throws IOException{
        usernameField.setPromptText("Email address");
        passwordField.setPromptText("Password");
        loginButton.setDisable(true);
        alert.setVisible(false);


    }

    @FXML
    private void login(Event event) throws IOException{
        // Try to establish connection as a user

        UserConnectionSingleton con = UserConnectionSingleton.getInstance();
        em = con.getManager();
        String username = usernameField.getText();
        String password = passwordField.getText();
        TypedQuery<User> tq2 = em.createNamedQuery(
                "User.findUserbyEmailPass",
                User.class);
        tq2.setParameter("email", username);
        tq2.setParameter("password", password);
        System.out.println(username+" "+password);

            // disabled Login for Testing purpose
            // User u3 = tq2.getSingleResult();

            //if user is logged in successfully, open the Home pag

        BorderPane root = new BorderPane();
        FXMLLoader loaderCenter = new FXMLLoader(getClass().getResource("/listComponent/list.fxml"));
        FXMLLoader loaderTop = new FXMLLoader(getClass().getResource("/barComponent/navbar.fxml"));
        FXMLLoader loaderLeftTop = new FXMLLoader(getClass().getResource("/filterComponent/filter.fxml"));
        FXMLLoader loaderLeftBottom = new FXMLLoader(getClass().getResource("/mapComponent/map.fxml"));
        FXMLLoader loaderRight = new FXMLLoader(getClass().getResource("/sidebarComponent/sidebar.fxml"));

        VBox leftBox = new VBox();
        leftBox.getChildren().add(loaderLeftTop.load());
        leftBox.getChildren().add(loaderLeftBottom.load());

        root.setCenter(loaderCenter.load());
        root.setRight(loaderRight.load());
        root.setLeft(leftBox);
        root.setTop(loaderTop.load());

        Scene scene = new Scene(root,1000,600);
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
