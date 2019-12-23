package settingsComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import handlers.UploadImage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainUI.MainPane;
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

    @FXML
    private Label removePhotoLabel;

    private Account currentAccount;
    private boolean profilePhotoChanged;
    private String defaultProfilePhotoURL = "https://i.imgur.com/EK2R1rn.jpg";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        profilePhotoChanged = false;

        String profileImageURL = currentAccount.getPicture();
        Image profileImage = new Image(profileImageURL);
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        if (profileImageURL.equals(defaultProfilePhotoURL)) {
            removePhotoLabel.setDisable(true);
        }

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
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/change_password.fxml"));
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
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png")
        );

        Stage window = Convenience.getWindow(mouseEvent);
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            String fileURI = selectedFile.toURI().toString();
            changeProfilePhoto(fileURI);
        }
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
            Convenience.closePreviousDialog();
        }
    }

    /**
     * Saves the user's current settings.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleSaveClicked(MouseEvent mouseEvent) {
        saveFirstName();
        saveLastName();
        saveProfilePhoto();

        showSuccess();
    }

    /**
     * Sets the default profile photo to the profile photo Circle.
     *
     * @param mouseEvent - the event that triggered the event
     */
    @FXML
    private void handleRemovePhotoClicked(MouseEvent mouseEvent) {
        if (userConfirmsRemovePhoto()) {
            changeProfilePhoto(defaultProfilePhotoURL);
        }
    }

    /**
     * Saves the user's current first name if it was changed.
     */
    private void saveFirstName() {
        String firstName = firstNameField.getText();
        if (!firstName.equals(currentAccount.getFirstname())) {
            EntityManager entityManager = currentAccount.getConnection();
            entityManager.getTransaction().begin();
            currentAccount.setFirstname(firstName);
            entityManager.getTransaction().commit();
        }
    }

    /**
     * Saves the user's current last name if it was changed.
     */
    private void saveLastName() {
        String lastName = lastNameField.getText();
        if (!lastName.equals(currentAccount.getLastname())) {
            EntityManager entityManager = currentAccount.getConnection();
            entityManager.getTransaction().begin();
            currentAccount.setLastname(lastName);
            entityManager.getTransaction().commit();
        }
    }

    /**
     * Saves the user's current profile photo if it was changed.
     */
    private void saveProfilePhoto() {
        if (profilePhotoChanged) {
            EntityManager entityManager = currentAccount.getConnection();
            entityManager.getTransaction().begin();

            ImagePattern imagePattern = (ImagePattern) profilePhotoCircle.getFill();
            Image profileImage = imagePattern.getImage();
            String imageURL = profileImage.getUrl();

            UploadImage imageUploader = new UploadImage(profileImage);

            try {
                if (!imageURL.equals(defaultProfilePhotoURL)) {
                    String profileImageURL = imageUploader.upload();
                    currentAccount.setPicture(profileImageURL);
                } else {
                    currentAccount.setPicture(defaultProfilePhotoURL);
                }
            } catch (Exception e) {
                Convenience.showAlert(Alert.AlertType.ERROR,
                        "Error", "Something went wrong", "Please, try again later");
            }

            entityManager.getTransaction().commit();
        }
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
            Convenience.closePreviousDialog();
            try {
                Convenience.switchScene(profilePhotoCircle, getClass().getResource("/FXML/mainUI.fxml"));
            } catch (IOException ioe) {
                Convenience.showAlert(Alert.AlertType.ERROR,
                        "Error", "Something went wrong", "Please, try again later");
            }
        }
    }

    /**
     * Changes the current photo in the profile photo Circle to the given image.
     *
     * @param imageURL - the URL of the image
     */
    private void changeProfilePhoto(String imageURL) {
        Image profilePhoto = new Image(imageURL);
        profilePhotoCircle.setFill(new ImagePattern(profilePhoto));

        if (imageURL.equals(defaultProfilePhotoURL)) {
            removePhotoLabel.setDisable(true);
        } else {
            removePhotoLabel.setDisable(false);
        }

        profilePhotoChanged = true;
    }

    /**
     * Pops up a dialog window to ask the user if he/she really wants to remove the profile photo.
     *
     * @return {@code true}, if the user presses the YES button. Otherwise, {@code false}.
     */
    private boolean userConfirmsRemovePhoto() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                Alert.AlertType.CONFIRMATION,
                "Confirmation", "Are you sure you want to remove your profile picture?", "",
                ButtonType.YES, ButtonType.CANCEL
        );

        return response.isPresent() && response.get() == ButtonType.YES;
    }
}
