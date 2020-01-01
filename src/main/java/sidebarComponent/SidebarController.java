package sidebarComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import authentification.GuestConnectionSingleton;
import authentification.RememberUserDBSingleton;
import com.jfoenix.controls.JFXButton;
import handlers.Convenience;
import handlers.HandleNet;
import handlers.LogOutHandler;
import mainUI.MainPane;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import models.Account;
import models.Admin;

import javax.naming.CommunicationException;
import javax.persistence.EntityManager;
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

    @FXML
    private JFXButton feedbackBtn;

    private boolean hidden = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
        String accountFirstName = currentAccount.getAccount().getFirstname();
        String accountLastName = currentAccount.getAccount().getLastname();
        String profilePhotoURL = currentAccount.getAccount().getPicture();

        usernameLabel.setText(accountFirstName + " " + accountLastName);
        Image profileImage = new Image(profilePhotoURL);
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        if (currentAccount.getAccount() instanceof Admin)
            feedbackBtn.setVisible(false);
    }

    /**
     * Opens the wishlist page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleWishlistClicked(MouseEvent mouseEvent) {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/wishlist.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }

    @FXML
    private void handleBookingHistoryClicked(MouseEvent mouseEvent) {
        // TODO: show the booking history
    }

    /**
     * Opens the settings page
     *
     * @param mouseEvent - the event which triggered the method
     * @throws IOException - can be thrown if the page could not be loaded
     */
    @FXML
    private void handleSettingsClicked(MouseEvent mouseEvent) throws IOException {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/settings.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
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
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
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
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/about.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }

    @FXML
    private void handleSupportClicked(MouseEvent mouseEvent) {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/contactForm.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }

    /**
     * Opens the feedback page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleFeedbackClicked(MouseEvent mouseEvent) throws IOException {
        try {
            if (HandleNet.hasNetConnection()) {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/feedback.fxml"));
            }
            else {
                throw new CommunicationException("No Internet");
            }
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            e.printStackTrace();
        }
        catch (CommunicationException e1){
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/noInternet.fxml"));
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
        if (userConfirmsLogOut()) {
            resetUserStatus();

            SidebarState.saveStateHidden(true);
            MainUiController.shutDownTasks();

            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/authentification.fxml"));
        }
    }

    /**
     * This method changes the user status from logged in -> logged out,
     * resets the system.
     */
    private void resetUserStatus(){
        Account account = CurrentAccountSingleton.getInstance().getAccount();
        LogOutHandler logOutHandler = new LogOutHandler(account);
        logOutHandler.handleLogOutProcess(false);
    }

    /**
     * Slides the sidebar to the left to show it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be visible to the user.
     */
    public void show() {
        slide(680);
        hidden = false;
    }

    /**
     * Slides the sidebar to the right to hide it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be hidden from the user.
     */
    public void hide() {
        slide(880);
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
     * @return {@code true}, if the user presses the YES button. Otherwise, {@code false}.
     */
    private boolean userConfirmsLogOut() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Are you sure you want to log out from your account?",
                ButtonType.YES, ButtonType.CANCEL
        );

        return response.isPresent() && response.get() == ButtonType.YES;
    }

    public boolean isHidden() {
        return hidden;
    }
}
