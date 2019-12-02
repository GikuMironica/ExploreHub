package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Account;
import models.Events;
import models.Transactions;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PreLoader implements Initializable {

    public StackPane stackrPane;
    public ImageView loadingScreen;
    private List<Transactions> transactionsList;
    private List<User> usersList;
    private List<Events> eventsList;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private FXMLLoader loader = new FXMLLoader();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadingScreen.setImage(new Image("/IMG/explorehubBW.jpg", 345,240, false, false));
        loader.setLocation(getClass().getResource("/FXML/controlPanel.fxml"));
        try {
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {

            loadTransactionStatistics();
            loadUserStatistics();
            loadEventStatistics();

            try {
                ControlPanelController controlPanelController = (ControlPanelController) loader.getController();
                Scene scene = new Scene(root);
                Platform.runLater(() -> {
                    stage.hide();
                    stage.setScene(scene);
                });
                Stage loadingStage = (Stage)loadingScreen.getScene().getWindow();
                controlPanelController.initialize(eventsList,transactionsList,usersList, stage, loadingStage);

            } catch (Exception e) {
                e.printStackTrace();
            }



        });
        thread.start();
    }

    public void loadUserStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<User> usersQuery;
        usersQuery = entityManager.createNamedQuery(
                "User.findAllUser",
                User.class);
        usersList = new ArrayList<>(usersQuery.getResultList());

    }

    public void loadTransactionStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Transactions> transactionsQuery;
        transactionsQuery = entityManager.createNamedQuery(
                "Transactions.findAllTransactions",
                Transactions.class);
        transactionsList = new ArrayList<>(transactionsQuery.getResultList());

    }

    public void loadEventStatistics(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Events> eventsQuery;
        eventsQuery = entityManager.createNamedQuery(
                "Events.findAllEvents",
                Events.class);
        eventsList = new ArrayList<>(eventsQuery.getResultList());

    }


    public void setScene(Scene scene){
        this.scene = scene;
        stage = (Stage)scene.getWindow();
    }

}
