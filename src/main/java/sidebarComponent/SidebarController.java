package sidebarComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import handlers.Convenience;
import handlers.HandleNet;
import handlers.LogOutHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mainUI.MainPane;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import models.User;

import javax.naming.CommunicationException;
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
    private VBox buttonContainer;

    @FXML
    private Label usernameLabel;

    @FXML
    private Circle profilePhotoCircle;

    private boolean hidden = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CurrentAccountSingleton currentAccount = CurrentAccountSingleton.getInstance();
        String accountFirstName = currentAccount.getAccount().getFirstname();
        String accountLastName = currentAccount.getAccount().getLastname();
        String profilePhotoURL = currentAccount.getAccount().getPicture();

        usernameLabel.setText(accountFirstName + " " + accountLastName);
        Image profileImage = new Image(profilePhotoURL);
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        if (currentAccount.getAccount() instanceof User) {
            addFeedbackButton();
        }
    }

    /**
     * Adds a feedback button to the sidebar.
     * This method is intended to be called only if the logged-in user is a regular user, and not an admin.
     */
    private void addFeedbackButton() {
        JFXButton feedbackButton = new JFXButton("Feedback");
        feedbackButton.setRipplerFill(Paint.valueOf("#f2f7f9"));
        feedbackButton.getStyleClass().addAll("sideBarText");
        feedbackButton.getStylesheets().addAll("/Styles/sidebar.css");
        feedbackButton.setOnMouseClicked(this::handleFeedbackClicked);
        buttonContainer.getChildren().add(6, feedbackButton);
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

    /**
     * Pops up the booking history list
     * @param mouseEvent {@link MouseEvent} event trigger
     */
    @FXML
    private void handleBookingHistoryClicked(MouseEvent mouseEvent) {
        try{
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/bookingHistory.fxml"));
        } catch (Exception e) {
            if (!HandleNet.hasNetConnection()) {
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                } catch (IOException ex) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            } else {
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }
        }
    }

    /**
     * Opens the settings page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleSettingsClicked(MouseEvent mouseEvent) {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/settings.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
            if (!HandleNet.hasNetConnection()) {
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                } catch (IOException ex) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            } else {
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }
        }
    }

    /**
     * Opens the FAQ page in a new window
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleFAQClicked(MouseEvent mouseEvent) {
        try {
            if (HandleNet.hasNetConnection()) {
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/faq.fxml"));
                Scene scene = new Scene(root);
                Stage window = new Stage();
                window.setScene(scene);
                window.initModality(Modality.APPLICATION_MODAL);
                window.setResizable(false);
                window.show();
            }
            else {
                throw new CommunicationException("No Internet");
            }
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            e.printStackTrace();
        }
        catch (CommunicationException e1){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            } catch (IOException ioe) {
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }

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
    private void handleFeedbackClicked(MouseEvent mouseEvent) {
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
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            } catch (IOException ioe) {
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }
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
        slide(6);
        hidden = false;
    }

    /**
     * Slides the sidebar to the right to hide it.
     * Calls the {@link #slide(int)} method with a specific position on the x-axis,
     * at which it will be hidden from the user.
     */
    public void hide() {
        slide(206);
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

    /**
     * Checks if the sidebar is hidden.
     *
     * @return {@code true} if the sidebar is hidden, {@code false} otherwise.
     */
    public boolean isHidden() {
        return hidden;
    }
}
