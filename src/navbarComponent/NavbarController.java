package navbarComponent;

import authentification.CurrentAccountSingleton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Account;
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
    private Button panelButton;

    @FXML
    private SidebarController sidebarController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Account currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        int accessLevel = currentAccount.getAccess();
        if (accessLevel == 1) {
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
            loadHomepage(mouseEvent);
        } catch (IOException ioe) {
            showError();
        }
    }

    /**
     * Opens the admin panel
     *
     * @param mouseEvent - the even which triggered the method
     */
    @FXML
    private void handlePanelClicked(MouseEvent mouseEvent) {
        // TODO: open admin's control panel
    }

    /**
     * Loads the homepage
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleHomeClicked(MouseEvent mouseEvent) {
        try {
            loadHomepage(mouseEvent);
        } catch (IOException ioe) {
            showError();
        }
    }

    /**
     * Opens the sidebar
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleMenuClicked(MouseEvent mouseEvent) {
        sidebarController.show();
    }

    /**
     * Loads the homepage.
     *
     * @param event - event which triggered the call to the method
     * @throws IOException - can be thrown if the main UI can't be loaded
     */
    private void loadHomepage(Event event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mainUI/mainUI.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Shows an error message to the user in case some unexpected errors occur.
     */
    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops... Unexpected error occurred.");
        alert.setContentText("Please try later.");

        alert.showAndWait();
    }
}
