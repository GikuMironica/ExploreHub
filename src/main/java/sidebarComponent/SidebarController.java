package sidebarComponent;

import authentification.AuthentificationController;
import authentification.CurrentAccountSingleton;
import authentification.GuestConnectionSingleton;
import authentification.RememberUserDBSingleton;
import handlers.Convenience;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import mainUI.MainUiController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class which controls the sidebar view
 * @author Hidayat Rzayev
 */
public class SidebarController implements Initializable {

    @FXML
    private AnchorPane sidebarPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private Circle profilePhotoCircle;

    private boolean hidden = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image profileImage = new Image("/IMG/icon-account.png");
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
        String accountFirstName = currentAccount.getAccount().getFirstname();
        String accountLastName = currentAccount.getAccount().getLastname();
        usernameLabel.setText(accountFirstName + " " + accountLastName);
    }

    /**
     * Opens the wishlist page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleWishlistClicked(MouseEvent mouseEvent) {
        try {
            Convenience.popupDialog(mouseEvent, getClass().getResource("/FXML/wishlist.fxml"), "Wishlist");
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
            ioe.printStackTrace();
        }
    }

    /**
     * Opens the settings page
     *
     * @param mouseEvent - the event which triggered the method
     * @throws IOException - can be thrown if the page could not be loaded
     */
    @FXML
    private void handleSettingsClicked(MouseEvent mouseEvent) throws IOException {
        Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/settings.fxml"));
    }

    /**
     * Opens the FAQ page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleFAQClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/faq.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Opens the about page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleAboutClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/about.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Opens the feedback page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleFeedbackClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/feedback.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * The current user will be logged out after being asked if he/she really wants to log out
     * and getting a positive reply.
     *
     * @param mouseEvent - the event which triggered the method
     * @throws IOException - can be thrown if the log out was not possible
     */
    @FXML
    private void handleLogOutClicked(MouseEvent mouseEvent) throws IOException {
        if (userWantsToLogOut()) {
            CurrentAccountSingleton.getInstance().getAccount().closeConnection();
            CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
            currentAccount.setAccount(null);
            AuthentificationController.stop();

            GuestConnectionSingleton.getInstance();
            RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
            userDB.cleanDB();

            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/authentification.fxml"));
        }
    }

    /**
     * Hides the sidebar
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleCloseClicked(MouseEvent mouseEvent) {
        hide();
    }

    /**
     * Slides the sidebar to the left to show it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be visible to the user.
     */
    public void show() {
        slide(600);
        hidden = false;
    }

    /**
     * Slides the sidebar to the right to hide it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be hidden from the user.
     */
    public void hide() {
        slide(800);
        hidden = true;
    }

    /**
     * Slides the {@link #sidebarPane} to a specific position on the x-axis.
     *
     * @param positionX - x position which the sidebar should be slided to.
     */
    private void slide(int positionX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), sidebarPane);
        transition.setToX(positionX);
        transition.play();
    }

    /**
     * Pops up a dialog window to ask the user if he/she really wants to log out.
     *
     * @return true, if the user presses the YES button. Otherwise, false.
     */
    private boolean userWantsToLogOut() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                Alert.AlertType.CONFIRMATION, "Confirmation",
                "Confirm Logout", "Are you sure you want to log out?",
                ButtonType.YES, ButtonType.CANCEL
        );

        return response.isPresent() && response.get() == ButtonType.YES;
    }

    public boolean isHidden() {
        return hidden;
    }
}
