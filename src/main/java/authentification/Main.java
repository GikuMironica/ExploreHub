package authentification;

import alerts.CustomAlertType;
import handlers.Convenience;
import handlers.LogOutHandler;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Account;
import persistenceComponent.GuestConnectionSingleton;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.ResultSet;

/**
 * Main method
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQueryApiInspection")
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
        primaryStage.setTitle("ExploreHub");
        primaryStage.setResizable(false);

        // checks if remember me was ticked
        if(userDB.okState()) {
            userDB.setUser();
            if(isLoggedIn()){
                Convenience.showAlert(CustomAlertType.WARNING,
                        "This user is already logged in. Log out from the other application first.");
                userDB.cleanDB();
                jumpLogin(primaryStage);
                return;
            }
            GuestConnectionSingleton.getInstance().closeConnection();
            AuthentificationController.initiliaseApp();
            Convenience.switchScene(primaryStage, getClass().getResource("/FXML/mainUI.fxml"));

        }else {
            jumpLogin(primaryStage);
        }
    }

    /**
     * This method returns the user from SQLDB
     * which were saved with "Remember Me " option
     * @return {@link Account} te user
     */
    private Account getUser(){
        try {
            EntityManager entityManager = GuestConnectionSingleton.getInstance().getManager();
            RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
            ResultSet result = userDB.getUser();
            String userID = "";
            Account account;

            while (result.next()) {
                userID = result.getString("Id");
            }

            account = entityManager.find(Account.class, Integer.valueOf(userID));
            return account;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method which checks if current user is already logged in from other device
     * @return {@link Boolean} true / false
     */
    private Boolean isLoggedIn(){
        try {
            EntityManager entityManager = GuestConnectionSingleton.getInstance().getManager();
            Account account = getUser();

            Query activeQuery = entityManager.createNamedQuery("Account.getStatusById", Account.class)
                    .setParameter("Id",account.getId());
            int active = (int)activeQuery.getSingleResult();

            return active == 1;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method which switches scene to main UI
     * @param stage {@link Stage} current stage
     * @throws Exception {@link java.io.IOException} ioException
     */
    private void jumpLogin(Stage stage) throws Exception{
        GuestConnectionSingleton.getInstance();
        StackPane mainRoot = new StackPane();
        BorderPane root = new BorderPane();
        mainRoot.getChildren().addAll(root);
        Convenience.switchScene(stage, getClass().getResource("/FXML/authentification.fxml"));
    }

    /**
     * Method closes the connection before exiting app
     */
    @Override
    public void stop(){
        if(CurrentAccountSingleton.getInstance().getAccount() == null){
            return;
        }else{
            Account account = CurrentAccountSingleton.getInstance().getAccount();
            LogOutHandler logOutHandler = new LogOutHandler(account);
            logOutHandler.handleLogOutProcess(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
