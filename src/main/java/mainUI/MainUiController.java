package mainUI;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import authentification.GuestConnectionSingleton;
import authentification.RememberUserDBSingleton;
import handlers.Convenience;
import handlers.LogOutHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import models.Account;
import sidebarComponent.SidebarState;

import javax.persistence.*;
import java.io.IOException;
import java.lang.annotation.Native;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class's responsibility is to handle the user's session
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
public class MainUiController implements Initializable {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private BorderPane mainBorderPane;

    private final String UPDATE_STATEMENT = "UPDATE users SET users.Active = 1 WHERE users.Id = ?";

    private static Thread destroyThread, countThread;
    private static ScheduledExecutorService executorService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainPane.getInstance().setStackPane(mainStackPane);
        MainPane.getInstance().setBorderPane(mainBorderPane);
        setLogin();
        startSession();
    }

    /**
     * This method changes the status of user to -> "logged in" in database
     */
    private void setLogin() {
        Account akk = CurrentAccountSingleton.getInstance().getAccount();
        EntityManager entityManager = akk.getConnection();
        int id = akk.getId();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(UPDATE_STATEMENT).setParameter(1, id).executeUpdate();
        entityManager.getTransaction().commit();

    }

    /**
     * This method creates a session for the current user with a custom
     * connection timeout
     */
    private void startSession(){
        Account akk = CurrentAccountSingleton.getInstance().getAccount();
        EntityManager entityManager = akk.getConnection();
        destroySession(entityManager, akk);
    }

    /**
     * This method will trigger destroySessionCountdown method which will
     * trigger a database event to logout the current user
     * @param entityManager {@link EntityManager} current active entity manager
     * @param akk {@link Account} current logged in account
     */
    private void destroySession(EntityManager entityManager, Account akk){
        int accountId = akk.getId();


        // create Scheduled Executor Task
        executorService = Executors.newScheduledThreadPool(1);
        Runnable logoutTask = () -> {

            // start DB event with 10 min delay
            destroyThread = new Thread(()-> destroySessionCountdown(entityManager, akk));
            destroyThread.start();

            // Freeze current thread for 9 minutes 540000
            try {
                // System.out.println(" waiting 1 min " + Thread.currentThread().getName());
                Thread.sleep(540000);

                Platform.runLater(() -> {

                    // FORK HERE
                    // wait 30 sec -> logout
                    startCounting();

                    // Pop up alert -> wait 30 seconds for user response
                    // System.out.println("Popup ready " + Thread.currentThread().getName());
                    Optional<ButtonType> response = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                            "Your session is about to end. Are you still with us? ", ButtonType.YES, ButtonType.NO);

                    // NO -> logout method + let DB event execute
                    if (response.isPresent() && response.get() == ButtonType.NO) {
                        countThread.stop();
                        countThread = null;
                        logOut();
                        // YES -> keep logged in + destroy DB event + kill startCounting Thread + Repeat
                    } else {
                        countThread.stop();
                        countThread = null;
                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                            System.out.println("Event deleted");
                            entityManager.createNativeQuery("DROP EVENT IF EXISTS session_event_" + accountId + ";").executeUpdate();
                        } else {
                            entityManager.createNativeQuery("DROP EVENT IF EXISTS session_event_" + accountId + ";").executeUpdate();
                        }
                        entityManager.getTransaction().commit();
                    }
                });
                // freeze 30 sec this Thread while user interacts with UI thread
                // System.out.println("freeze task while user interacting with FX thread on " + Thread.currentThread().getName());
                Thread.sleep(30000);
             }catch(InterruptedException iexception){ // it's going to be interrupted!
             }catch(Exception e){ e.printStackTrace();/* sleep interupted exception*/}

        };
        // repeat this task
        executorService.scheduleWithFixedDelay(logoutTask, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Method which will force logout if the user doesn't respond
     * to the dialog with response which offers to continue with current session
     */
    private void startCounting(){
        Runnable run = () -> {
            try {
          //      System.out.println("startCounting thread executed "+Thread.currentThread().getName());
                Thread.sleep(30000);
            }catch (Exception e){ }
          //  System.out.println("StartCounting executing");
            try {
                Platform.runLater(() -> {
                    logOut();
                });
            }catch (Exception ex){
                // null pointer -> ....
            }
        };
        countThread = new Thread(run);
        countThread.start();
    }

    /**
     * Method which force logout the user when session expired
     */
    private void logOut(){
        Account account = CurrentAccountSingleton.getInstance().getAccount();
        LogOutHandler logOutHandler = new LogOutHandler(account);
        logOutHandler.handleLogOutProcess(false);

        // destroy scheduled task
        executorService.shutdownNow();
        executorService = null;

        SidebarState.saveStateHidden(true);
        try {
            Convenience.switchScene(mainStackPane, getClass().getResource("/FXML/authentification.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is meant to destroy the session 15 minutes after it wwas started
     * User will be forced to log out
     * @param entityManager {@link EntityManager} current active entity manager
     * @param akk {@link Account} current logged in account
     */
    public void destroySessionCountdown(EntityManager entityManager, Account akk){
        int accountId = akk.getId();

        try {
          //  System.out.println("Event started");
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("DROP EVENT IF EXISTS session_event_"+accountId+";").executeUpdate();
            entityManager.createNativeQuery("CREATE EVENT IF NOT EXISTS session_event_"+accountId+" ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 10 MINUTE DO UPDATE users SET users.Active = 0 WHERE users.Id = ?")
                    .setParameter(1,accountId).executeUpdate();
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Shut down all background tasks before loggin out.
     */
    public static void shutDownTasks(){
        try {
            executorService.shutdownNow();
            executorService = null;
            destroyThread.stop();
            destroyThread = null;
          //  System.out.println("tasks killed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * This method is called whenever the user clicks anywhere on the main UI.
     * The main UI will get the focus if this event occurs, making other UI elements
     * lose their focus.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleMainUiPressed(MouseEvent mouseEvent) {
        mainBorderPane.requestFocus();
    }
}

