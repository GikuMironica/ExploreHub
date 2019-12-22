package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import mainUI.MainPane;
import models.*;
import java.io.IOException;
import java.util.List;

/**
 * Public class which controlls the tab pane of control panel.
 *
 * @author Aleksejs Marmiss
 */
public class ControlPanelController {
    public TabPane tabPane;
    public AnchorPane manageUsers;
    public AnchorPane managePayments;
    public AnchorPane manageEvents;
    public AnchorPane manageAdmins;
    public AnchorPane communication;
    private FXMLLoader statisticsLoader = new FXMLLoader();
    private FXMLLoader manageUsersLoader = new FXMLLoader();
    private FXMLLoader managePaymentsLoader = new FXMLLoader();
    private FXMLLoader manageEventsLoader = new FXMLLoader();
    private FXMLLoader manageAdminsLoader = new FXMLLoader();
    private FXMLLoader communicationLoader = new FXMLLoader();
    private boolean animationFinished = false;
    private boolean loadingFinished = false;
    @FXML
    private AnchorPane statisticsTab;
    @FXML
    private Tab adminsTab;

    /**
     * Methos which initializes and populates the control panel and all tabs.
     * @param eventsList list of Events objects.
     * @param transactionsList list of transactions objects.
     * @param usersList list of user objects.
     * @param preloader link to the preloader controller.
     */
    synchronized public void initialize(List<Events> eventsList, List<Transactions> transactionsList, List<User> usersList, PreLoader preloader) {
        statisticsLoader.setLocation(getClass().getResource("/FXML/statisticsTab.fxml"));
        manageUsersLoader.setLocation(getClass().getResource("/FXML/manageUsersTab.fxml"));
        managePaymentsLoader.setLocation(getClass().getResource("/FXML/managePaymentsTab.fxml"));
        manageEventsLoader.setLocation(getClass().getResource("/FXML/manageEventsTab.fxml"));
        manageAdminsLoader.setLocation(getClass().getResource("/FXML/manageAdmins.fxml"));
        communicationLoader.setLocation(getClass().getResource("/FXML/communicationTab.fxml"));
        StackPane statisticsContent;
        AnchorPane manageUsersContent;
        AnchorPane managePaymentsContent;
        AnchorPane manageEventsContent;
        AnchorPane manageAdminsContent;
        StackPane communicationContent;
        try {
            statisticsContent = (StackPane) statisticsLoader.load();
            manageUsersContent = (AnchorPane) manageUsersLoader.load();
            managePaymentsContent = (AnchorPane) managePaymentsLoader.load();
            manageEventsContent = (AnchorPane) manageEventsLoader.load();
            manageAdminsContent = manageAdminsLoader.load();
            communicationContent = (StackPane) communicationLoader.load();
            StatisticsController statisticsController = (StatisticsController) statisticsLoader.getController();
            ManageUsersTabController manageUsersTabController = (ManageUsersTabController) manageUsersLoader.getController();
            ManagePaymentsTabController managePaymentsTabController = (ManagePaymentsTabController) managePaymentsLoader.getController();
            ManageEventsTabController manageEventsTabController = (ManageEventsTabController) manageEventsLoader.getController();
            ManageAdminsTabController manageadminsTabController = (ManageAdminsTabController) manageAdminsLoader.getController();
            CommunicationTabController communicationTabController = (CommunicationTabController) communicationLoader.getController();
            manageEventsTabController.initialize(eventsList);
            managePaymentsTabController.initialize();
            statisticsController.initialize(eventsList, usersList, transactionsList);
            manageUsersTabController.setUsers(usersList);
            manageadminsTabController.initialize();
            communicationTabController.initialize();
            Task<Void> task = new Task<Void>() {
                @Override
                protected void succeeded() {
                    super.succeeded();
                    loadingFinished = true;
                    setLoadingFinished();
                }

                @Override
                protected Void call(){
                    statisticsTab.getChildren().setAll(statisticsContent);
                    manageUsers.getChildren().setAll(manageUsersContent);
                    managePayments.getChildren().setAll(managePaymentsContent);
                    manageEvents.getChildren().setAll(manageEventsContent);
                    manageAdmins.getChildren().setAll(manageAdminsContent);
                    communication.getChildren().setAll(communicationContent);
                    return null;
                }
            };
            task.run();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTabVisible(){
        Account account = CurrentAccountSingleton.getInstance().getAccount();
        TabPane tp = adminsTab.getTabPane();
        if(!(account instanceof Owner)){
            tp.getTabs().remove(adminsTab);
            tp.setTabMaxWidth(174);
            tp.setTabMinWidth(174);
        }
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

     public void setLoadingFinished(){
       if (animationFinished && loadingFinished) {
           Platform.runLater(() -> {
               MainPane.getInstance().getStackPane().getChildren().setAll(tabPane);
               setTabVisible();
           });
       }
    }

    public void setAnimationFinished(boolean animationFinished) {
        this.animationFinished = animationFinished;
        setLoadingFinished();
    }
}
