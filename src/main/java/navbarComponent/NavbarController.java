package navbarComponent;

import authentification.CurrentAccountSingleton;
import controlPanelComponent.PreLoader;
import handlers.Convenience;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import listComponent.EventListSingleton;
import models.Account;
import models.Admin;
import sidebarComponent.SidebarController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class which controls the navigation bar (toolbar)
 * @author Hidayat Rzayev
 */
public class NavbarController implements Initializable {

    @FXML
    private Button panelButton, refreshButton;

    @FXML
    private SidebarController sidebarController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Account currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        int accessLevel = currentAccount.getAccess();
        if (currentAccount instanceof Admin) {
            panelButton.setVisible(true);
        }
    }

    /**
     * Loads the homepage
     *
     * @param mouseEvent - the even which triggered the method
     */
    @FXML
    private void handleTitleClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Opens the admin panel
     *
     * @param mouseEvent - the even which triggered the method
     */
    @FXML
    private void handlePanelClicked(MouseEvent mouseEvent) throws IOException{
        //Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/PreLoader.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PreLoader.fxml"));
        Parent root = (Parent) loader.load();
        PreLoader controller = (PreLoader) loader.getController();
        controller.setScene(((Node) mouseEvent.getSource()).getScene());
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Stage window = new Stage();
        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Loading");
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
       // stage.hide();
    }

    /**
     * Loads the homepage
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleHomeClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * If the sidebar is hidden, then it will be shown. Otherwise it will be hidden.
     * Loads the Discussion page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleDiscussionClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/discussionView.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
            ioe.printStackTrace();
        }
    }

    /**
     * Opens the sidebar
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleMenuClicked(MouseEvent mouseEvent) {
        if (sidebarController.isHidden()) {
            sidebarController.show();
        } else {
            sidebarController.hide();
        }
    }

    /**
     * This method is called whenever the user clicks anywhere on the navbar.
     * If the navbar is not hidden, it will hide it.
     *
     * @param mouseEvent - the event which triggered the method
     */
//    @FXML
//    private void handleNavbarClicked(MouseEvent mouseEvent) {
//        System.out.println("Navbar CLICKED!!!");
//        if (!sidebarController.isHidden()) {
//            sidebarController.hide();
//        }
//    }

    @FXML
    private void handleRefreshClicked(MouseEvent mouseEvent){
        EventListSingleton eventList = EventListSingleton.getInstance();
        eventList.refreshList();

        refreshButton.setDisable(true);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(5)
        );

        visiblePause.setOnFinished(
                (ActionEvent ev) -> {
                    refreshButton.setDisable(false);
                }
        );
        visiblePause.play();
    }

}
