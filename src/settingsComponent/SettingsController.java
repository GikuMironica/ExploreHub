package settingsComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Account;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class which controls the Settings view
 * @author Hidayat Rzayev
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Circle profilePhotoCircle;

    private Account currentAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();

        Image profileImage = new Image("file:////../res/icon-account.png");
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        firstNameField.setText(currentAccount.getFirstname());
        lastNameField.setText(currentAccount.getLastname());
        emailField.setText(currentAccount.getEmail());
        passwordField.setText(currentAccount.getPassword());
    }

    /**
     * Opens the page where the user can change the password
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleChangePasswordClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent,
                    getClass().getResource("/changePasswordComponent/change_password.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Opens the file explorer, from where the user can choose a profile photo.
     * Supported image formats: JPEG, PNG
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleChangePhotoClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );

        Stage window = Convenience.getWindow(mouseEvent);
        File selectedFile = fileChooser.showOpenDialog(window);

        System.out.println("Selected file: " + selectedFile);
    }

    /**
     * Warns the user that unsaved changes will be lost after cancellation.
     * If the user wants to cancel anyway, then no changes will be performed and the homepage will be loaded.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleCancelClicked(MouseEvent mouseEvent) {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                Alert.AlertType.CONFIRMATION,
                "Confirmation", "Unsaved changes will be lost", "Do you want to continue?",
                ButtonType.YES, ButtonType.CANCEL
        );

        if (response.isPresent() && response.get() == ButtonType.YES) {
            try {
                Convenience.switchScene(mouseEvent, getClass().getResource("/mainUI/mainUI.fxml"));
            } catch (IOException e) {
                Convenience.showAlert(Alert.AlertType.ERROR,
                        "Error", "Something went wrong", "Please, try again later");
            }
        }
    }

    /**
     * Updates a user's first and last names in the database if they were changed.
     * In the end lets the user know that the changes have been saved.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleSaveClicked(MouseEvent mouseEvent) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();

        if (!firstName.equals(currentAccount.getFirstname())) {
            EntityManager entityManager = currentAccount.getConnection();
            entityManager.getTransaction().begin();
            currentAccount.setFirstname(firstName);
            entityManager.getTransaction().commit();
        }

        if (!lastName.equals(currentAccount.getLastname())) {
            EntityManager entityManager = currentAccount.getConnection();
            entityManager.getTransaction().begin();
            currentAccount.setLastname(lastName);
            entityManager.getTransaction().commit();
        }

        showSuccess();
    }

    /**
     * Shows the user that the changes have been saved and asks if he/she wants to return to homepage.
     * If yes, the homepage will be loaded. Otherwise, the page will not be changed.
     */
    private void showSuccess() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                Alert.AlertType.INFORMATION,
                "Information", "Your changes have been saved!", "Return to homepage?",
                ButtonType.YES, ButtonType.CANCEL
        );

        if (response.isPresent() && response.get() == ButtonType.YES) {
            try {
                Convenience.switchScene(profilePhotoCircle, getClass().getResource("/mainUI/mainUI.fxml"));
            } catch (IOException e) {
                Convenience.showAlert(Alert.AlertType.ERROR,
                        "Error", "Something went wrong", "Please, try again later");
            }
        }
    }
}
