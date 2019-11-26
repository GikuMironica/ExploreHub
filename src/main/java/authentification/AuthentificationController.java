package authentification;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import listComponent.EventListSingleton;
import listComponent.UpdateListTask;
import models.Account;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.Timer;

/**
 * Class which handles the authentification process
 *
 * @author Gheorghe Mironica, Tonislav Tachev
 */
public class AuthentificationController {

    @FXML
    private JFXCheckBox rememberBox;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private JFXTextField alert;
    private Strategy loginStrategy;
    private EntityManager entityManager;
    private static Timer timer;
    private static UpdateListTask updateTask;
    private static long delay = 0;
    private static long interval = 15000;

    /**
     * Method which initializes the views
     *
     * @throws IOException {@link IOException}
     */
    public void init() throws IOException{
        usernameField.setPromptText("Email address");
        passwordField.setPromptText("Password");
        loginButton.setDisable(true);
        alert.setVisible(false);
        alert.setVisible(false);
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event){
                if(event.getCode().equals(KeyCode.ENTER)){
                    loginButton.fire();
                }
            }
        });
    }

    /**
     * Method which handles the login process
     *
     * @param event method trigger {@link Event}
     * @throws IOException {@link IOException}
     */
    @FXML
    private void login(Event event) throws IOException{
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            // Try to establish connection as a user
            GuestConnectionSingleton con = GuestConnectionSingleton.getInstance();
            entityManager = con.getManager();
            @SuppressWarnings("JpaQueryApiInspection")
            Query tq1 = entityManager.createNamedQuery(
                    "User.determineAccess",
                    User.class);
            tq1.setParameter("email", username);
            tq1.setParameter("password", password);
            int i = (int)tq1.getSingleResult();

            // Using Strategy Pattern to pass a unique instance of User to The Singleton
            if(i==0) {
                loginStrategy = new UserStrategy();
                loginStrategy.getAccount(username, password);
            }
            else {
                loginStrategy = new AdminStrategy();
                loginStrategy.getAccount(username, password);
            }

            initiliaseApp();
            GuestConnectionSingleton.getInstance().getManager().close();
            GuestConnectionSingleton.getInstance().closeConnection();

        }catch(Exception e){
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
        alert.setVisible(false);

        // check if checkbox clicked
        checkRememberBox(username, password);

        //if user is logged in successfully, open the Home pag
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/mainUI.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    /**
     * This method initialises main application background jobs
     */
    private static void initiliaseApp() {
        //Initialize listView in a separate Thread
        Thread thread = new Thread(() -> {
            EventListSingleton ev = EventListSingleton.getInstance();
        });
        thread.start();

        // Start Job to update regularly the ListView in background
        timer = new Timer();
        updateTask = new UpdateListTask();
        timer.scheduleAtFixedRate(updateTask, delay, interval);
    }

    /**
     * Saves user credentials on demand
     *
     * @param user user email {@link String}
     * @param pass user password {@link String}
     */
    private void checkRememberBox(String user, String pass) {
        RememberUserDBSingleton userDBSingleton = RememberUserDBSingleton.getInstance();

        if(rememberBox.isSelected()){
            userDBSingleton.init(user, pass);
            userDBSingleton.setUser();
        } else{
            userDBSingleton.cleanDB();
        }
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

    /**
     * Method which handles the register process
     *
     * @param event method trigger {@link Event}
     * @throws IOException {@link IOException}
     */
    @FXML
    private void register(Event event) throws IOException {
        //Check the internet connection first
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/register.fxml"));
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

    /**
     * Method which handles enter button pressed
     *
     * @param ae method triggers {@link Event}
     */
    @FXML
    public void onEnter(ActionEvent ae){
    }

    /**
     * Method which handles the recovery process
     *
     * @param event method trigger {@link Event}
     * @throws IOException {@link IOException}
     */
    @FXML
    private void recover(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/recover.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Method which stops the Scheduled Background Job before exiting app
     */
    public static void stop(){
        timer.cancel();
        timer.purge();
    }
}
