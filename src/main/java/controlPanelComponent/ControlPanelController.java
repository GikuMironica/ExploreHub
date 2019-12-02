package controlPanelComponent;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Events;
import models.Transactions;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControlPanelController {

    public AnchorPane manageUsers;
    public AnchorPane managePayments;
    public AnchorPane manageEvents;
    private FXMLLoader statisticsLoader = new FXMLLoader();
    private FXMLLoader manageUsersLoader = new FXMLLoader();
    private FXMLLoader managePaymentsLoader = new FXMLLoader();
    private FXMLLoader manageEventsLoader = new FXMLLoader();
    @FXML
    private AnchorPane statisticsTab;

    public void initialize(List<Events> eventsList, List<Transactions> transactionsList, List<User> usersList, Stage stage, Stage loadingStage) {
        statisticsLoader.setLocation(getClass().getResource("/FXML/statisticsTab.fxml"));
        manageUsersLoader.setLocation(getClass().getResource("/FXML/manageUsersTab.fxml"));
        managePaymentsLoader.setLocation(getClass().getResource("/FXML/managePaymentsTab.fxml"));
        manageEventsLoader.setLocation(getClass().getResource("/FXML/manageEventsTab.fxml"));
        StackPane statisticsContent;
        AnchorPane manageUsersContent;
        AnchorPane managePaymentsContent;
        AnchorPane manageEventsContent;
        try {
            statisticsContent = (StackPane) statisticsLoader.load();
            manageUsersContent = (AnchorPane) manageUsersLoader.load();
            managePaymentsContent = (AnchorPane) managePaymentsLoader.load();
            manageEventsContent = (AnchorPane) manageEventsLoader.load();
            StatisticsController statisticsController = (StatisticsController) statisticsLoader.getController();
            ManageUsersTabController manageUsersTabController = (ManageUsersTabController) manageUsersLoader.getController();
            ManagePaymentsTabController managePaymentsTabController = (ManagePaymentsTabController) managePaymentsLoader.getController();
            ManageEventsTabController manageEventsTabController = (ManageEventsTabController) manageEventsLoader.getController();
            manageEventsTabController.initialize();
            managePaymentsTabController.initialize();
            statisticsController.initialize(eventsList, usersList, transactionsList);
            manageUsersTabController.setUsers(usersList);
            statisticsTab.getChildren().setAll(statisticsContent);
            manageUsers.getChildren().setAll(manageUsersContent);
            managePayments.getChildren().setAll(managePaymentsContent);
            manageEvents.getChildren().setAll(manageEventsContent);
            Platform.runLater(() -> {
                    stage.show();
                    loadingStage.close();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
