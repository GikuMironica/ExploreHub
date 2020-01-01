package controlPanelComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import mainUI.MainPane;
import models.Account;
import models.Events;
import models.Transactions;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Preloader class which makes database requests while displaying loading screen.
 *
 * @author Aleksejs Marmiss
 */
public class PreLoader {

    public StackPane stackrPane;
    private List<Transactions> transactionsList;
    private List<User> usersList;
    private List<Events> eventsList;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private FXMLLoader loader = new FXMLLoader();
    private JFXDialog dialog;
    @FXML
    private JFXSpinner progress;
    private ControlPanelController controlPanelController;
    private Timeline timeline;

    /**
     * Method which initializes preloader.
     */
    public void initialization() {
        loader.setLocation(getClass().getResource("/FXML/controlPanel.fxml"));
        try {
            loader.load();
        } catch (Exception e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
        Thread thread = new Thread(() -> {
            controlPanelController = (ControlPanelController) loader.getController();
            startAnimation();
            if(HandleNet.hasNetConnection()) {
                try {
                    loadTransactionStatistics();
                    loadUserStatistics();
                    loadEventStatistics();
                }catch (Exception internetLost){
                    throwNoInternetAlert();
                    internetLost.printStackTrace();
                    Thread.currentThread().interrupt();
                    return;
                }
            }else{
                throwNoInternetAlert();
                return;
            }
            controlPanelController.initialize(eventsList,transactionsList,usersList, dialog);
        });
        thread.start();
        progress.progressProperty().set(1);
    }

    /**
     * Method which loads users from database.
     */
    public void loadUserStatistics() throws Exception{
        EntityManager entityManager = admin.getConnection();
        TypedQuery<User> usersQuery;
        usersQuery = entityManager.createNamedQuery(
                "User.findAllUser",
                User.class);
        if(!HandleNet.hasNetConnection()){
            throw new Exception("Internet Connection lost");
        }
        usersList = new ArrayList<>(usersQuery.getResultList());
    }
    /**
     * Method which loads transactionjs from database.
     */
    public void loadTransactionStatistics() throws Exception{
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Transactions> transactionsQuery;
        transactionsQuery = entityManager.createNamedQuery(
                "Transactions.findAllTransactions",
                Transactions.class);
        if(!HandleNet.hasNetConnection()){
            throw new Exception("Internet Connection lost");
        }
        transactionsList = new ArrayList<>(transactionsQuery.getResultList());



    }
    /**
     * Method which loads events from database.
     */
    public void loadEventStatistics() throws Exception{
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Events> eventsQuery;
        eventsQuery = entityManager.createNamedQuery(
                "Events.findAllEvents",
                Events.class);
        if(!HandleNet.hasNetConnection()){
            throw new Exception("Internet Connection lost");
        }
        eventsList = new ArrayList<>(eventsQuery.getResultList());
    }

    /**
     * Method that allows to set the loading dialog from the outside of the controller.
     * @param dialog dialog which has to be used by a instance of controller.
     */
    public void setLoading(JFXDialog dialog){
        this.dialog = dialog;
    }

  synchronized private void startAnimation(){
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(Duration.millis(2500),
                        new KeyValue(progress.progressProperty(), 0.5)),
                new KeyFrame(Duration.millis(5000),
                        new KeyValue(progress.progressProperty(), 0.99)));

        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(event -> controlPanelController.setAnimationFinished(true));
    }

    private void throwNoInternetAlert(){
           Platform.runLater(new Runnable() {
               @Override
               public void run() {
                   try {
                       timeline.stop();
                       dialog.close();
                        Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(), getClass().getResource("/FXML/noInternet.fxml"));
                   }catch(Exception exc){
                       Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                    }
               }
           });

    }


}
