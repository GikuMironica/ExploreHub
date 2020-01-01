package authentification;

import alerts.CustomAlertType;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import handlers.Convenience;
import handlers.EntityManagerEditor;
import handlers.HandleNet;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import listComponent.EventListSingleton;
import models.Account;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.Session;
import persistenceComponent.GuestConnectionSingleton;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class which handles the authentification process
 *
 * @author Gheorghe Mironica, Tonislav Tachev
 */
@SuppressWarnings("JpaQueryApiInspection")
public class AuthentificationController implements Initializable {

    @FXML
    private StackPane authStackPane;
    @FXML
    private AnchorPane authAnchorPane;
    @FXML
    private JFXCheckBox rememberBox;
    @FXML
    private TextField usernameField, passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private JFXTextField alert;
    private EntityManager entityManager;

    /**
     * Method which initializes the views
     *
     * @throws IOException {@link IOException}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
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

        StrategyContext strategyContext;
        String username = usernameField.getText().toLowerCase();
        String password = passwordField.getText();
        int active = 0;

        try {
            int accessLvl = getUserAccessLvl(username, password);

            switch(accessLvl){
                case 2: strategyContext = new StrategyContext(new OwnerStrategy()); break;
                case 1: strategyContext = new StrategyContext(new AdminStrategy()); break;
                case 0: strategyContext = new StrategyContext(new UserStrategy()); break;
                default: strategyContext = null;
            }

            strategyContext.executeStrategy(username, password);
            Account account = CurrentAccountSingleton.getInstance().getAccount();

            Query activeQuery = entityManager.createNamedQuery("Account.getStatusById", Account.class)
                    .setParameter("Id",account.getId());
            active = (int)activeQuery.getSingleResult();

            if(active==1) {
                Convenience.showAlert(CustomAlertType.WARNING,
                        "This user is already logged in. Log out from the other application first.");
                return;
            }

            checkRememberBox(account.getId());
            GuestConnectionSingleton.getInstance().closeConnection();

            initiliaseApp();

            // connection attempts
            EntityManager newManager = account.getConnection();
            Session session =((JpaEntityManager)newManager.getDelegate()).getActiveSession();
            EntityManagerEditor customizer = new EntityManagerEditor();
            customizer.customize(session);

        }catch(Exception e){
            if(!HandleNet.hasNetConnection()){
                Convenience.popupDialog(authStackPane, authAnchorPane, getClass().getResource("/FXML/noInternet.fxml"));
            }else {
                alert.setText("Invalid Email or Password");
                alert.setVisible(true);
                usernameField.clear();
                passwordField.clear();

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
        }
        alert.setVisible(false);
        Convenience.switchScene(event, getClass().getResource("/FXML/mainUI.fxml"));
    }


    /**
     * Method which checks if user exists, returns access level
     *
     * @param user username {@link String}
     * @param pass password {@link String}
     * @return {@link Integer}
     */
    private int getUserAccessLvl(String user, String pass) {
            // Try to establish connection as a guest
            GuestConnectionSingleton con = GuestConnectionSingleton.getInstance();
            entityManager = con.getManager();
            @SuppressWarnings("JpaQueryApiInspection")
            Query tq1 = entityManager.createNamedQuery(
                    "Account.determineAccess",
                    Account.class);
            tq1.setParameter("email", user);
            tq1.setParameter("password", pass);
            return (int) tq1.getSingleResult();

    }

    /**
     * This method initialises main application in a new parallel thread
     */
    public static void initiliaseApp(){
        EventListSingleton eventList = EventListSingleton.getInstance();
        eventList.refreshList();
    }

    /**
     * Saves user credentials on demand
     *
     * @param accountID {@link Integer account ID}
     */
    private void checkRememberBox(int accountID) {
        RememberUserDBSingleton userDBSingleton = RememberUserDBSingleton.getInstance();

        if(rememberBox.isSelected()){
            userDBSingleton.init(accountID);
            userDBSingleton.setUser();
            GuestConnectionSingleton.getInstance().closeConnection();
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

        try {
            Convenience.switchScene(event, getClass().getResource("/FXML/register.fxml"));
        } catch(Error e){
            Convenience.popupDialog(authStackPane, authAnchorPane, getClass().getResource("/FXML/noInternet.fxml"));
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
        Convenience.popupDialog(authStackPane, authAnchorPane, getClass().getResource("/FXML/recover.fxml"));
    }


}
