package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSpinner;
import handlers.HandleNet;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
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

    /**
     * Method which initializes preloader.
     */
    public void initialization() {
        loader.setLocation(getClass().getResource("/FXML/controlPanel.fxml"));

        setProgress(0.05);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            controlPanelController = (ControlPanelController) loader.getController();
            startAnimation();
            loadTransactionStatistics();
            loadUserStatistics();
            loadEventStatistics();
            controlPanelController.initialize(eventsList,transactionsList,usersList, this);
        });
        thread.start();
    }

    /**
     * Method which loads users from database.
     */
    public void loadUserStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<User> usersQuery;
        usersQuery = entityManager.createNamedQuery(
                "User.findAllUser",
                User.class);
        if(!HandleNet.hasNetConnection()){
            //TODO
        }
        usersList = new ArrayList<>(usersQuery.getResultList());

    }
    /**
     * Method which loads transactionjs from database.
     */
    public void loadTransactionStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Transactions> transactionsQuery;
        transactionsQuery = entityManager.createNamedQuery(
                "Transactions.findAllTransactions",
                Transactions.class);
        if(!HandleNet.hasNetConnection()){
            //TODO
        }
        transactionsList = new ArrayList<>(transactionsQuery.getResultList());

    }
    /**
     * Method which loads events from database.
     */
    public void loadEventStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Events> eventsQuery;
        eventsQuery = entityManager.createNamedQuery(
                "Events.findAllEvents",
                Events.class);
        if(!HandleNet.hasNetConnection()){
            //TODO
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

    /**
     * Method that allows to set the loading progress from the outside of the controller.
     * @param value loading progress.
     */
    public void setProgress(double value){
        Platform.runLater(() -> {
           progress.progressProperty().set(value);
        });
    }

  synchronized private void startAnimation(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progress.progressProperty(), 0)),
                new KeyFrame(Duration.millis(2500),
                        new KeyValue(progress.progressProperty(), 0.5)),
                new KeyFrame(Duration.millis(5000),
                        new KeyValue(progress.progressProperty(), 1)));

        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(event -> controlPanelController.setAnimationFinished(true));
    }

}
