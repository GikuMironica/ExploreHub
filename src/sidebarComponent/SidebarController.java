package sidebarComponent;

import authentification.CurrentAccountSingleton;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image profileImage = new Image("file:////../res/icon-account.png");
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
            switchScene("/wishlistComponent/wishlist.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
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
        Parent root = FXMLLoader.load(getClass().getResource("/settingsComponent/settings.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Opens the FAQ page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleFAQClicked(MouseEvent mouseEvent) {
        try {
            switchScene("/FAQComponent/faq.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
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
            switchScene("/aboutComponent/about.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
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
            switchScene("/feedbackComponent/feedback.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
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
            CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
            currentAccount.setAccount(null);

            Parent root = FXMLLoader.load(getClass().getResource("/authentification/authentification.fxml"));
            Scene scene = new Scene(root, 600, 400);
            Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
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
    }

    /**
     * Slides the sidebar to the right to hide it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be hidden from the user.
     */
    private void hide() {
        slide(800);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to log out?");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    /**
     * Switches the current page
     *
     * @param resourcePath - path to the fxml file which is to be loaded
     * @throws IOException - if the resource could not be loaded
     */
    private void switchScene(String resourcePath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
        Scene scene = new Scene(root);
        Stage window = (Stage) sidebarPane.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
