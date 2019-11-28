package navbarComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
        Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/controlPanel.fxml"));
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
}
