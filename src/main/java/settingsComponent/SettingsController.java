package settingsComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import handlers.Convenience;
import handlers.UploadImage;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public static final String DEFAULT_PROFILE_PHOTO_URL = "https://i.imgur.com/EK2R1rn.jpg";

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private JFXButton saveButton;

    @FXML
    private Circle profilePhotoCircle;

    @FXML
    private Label removePhotoLabel;

    @FXML
    private JFXSpinner spinner;

    private Account currentAccount;
    private boolean profilePhotoChanged;
    private Task<String> saveProfilePhotoTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        profilePhotoChanged = false;

        String profileImageURL = currentAccount.getPicture();
        Image profileImage = new Image(profileImageURL);
        profilePhotoCircle.setFill(new ImagePattern(profileImage));

        if (profileImageURL.equals(DEFAULT_PROFILE_PHOTO_URL)) {
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
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
            e.printStackTrace();
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
                new FileChooser.ExtensionFilter("Images", "*.jpeg", "*.jpg", "*.png")
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
        if (!isAnythingChanged()) {
            Convenience.closePreviousDialog();
            return;
        }

        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Any unsaved changes will be lost. Do you want to continue?",
                ButtonType.YES, ButtonType.CANCEL
        );

        if (response.isPresent() && response.get() == ButtonType.YES) {
            if (saveProfilePhotoTask != null && saveProfilePhotoTask.isRunning()) {
                saveProfilePhotoTask.cancel();
            }
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
        if (!isAnythingChanged()) {
            return;
        }

        EntityManager entityManager = currentAccount.getConnection();
        entityManager.getTransaction().begin();
        saveProfilePhotoTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return saveProfilePhoto();
            }
        };
        saveProfilePhotoTask.setOnSucceeded(workerStateEvent -> {
            String profileImageURL = saveProfilePhotoTask.getValue();
            if (profileImageURL != null && !profileImageURL.isBlank()) {
                currentAccount.setPicture(profileImageURL);
            } else if (profileImageURL != null) {
                Convenience.showAlert(CustomAlertType.WARNING, "This image can't be uploaded due to its size");
                entityManager.getTransaction().rollback();
                return;
            }

            saveFirstName();
            saveLastName();
            entityManager.getTransaction().commit();

            try {
                showSuccess();
            } catch (IOException ioe) {
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }
        });

        saveProfilePhotoTask.setOnCancelled(workerStateEvent -> {
            entityManager.getTransaction().rollback();
        });
        spinner.visibleProperty().bind(saveProfilePhotoTask.runningProperty());
        saveButton.disableProperty().bind(saveProfilePhotoTask.runningProperty());

        new Thread(saveProfilePhotoTask).start();
    }

    /**
     * Sets the default profile photo to the profile photo Circle.
     *
     * @param mouseEvent - the event that triggered the event
     */
    @FXML
    private void handleRemovePhotoClicked(MouseEvent mouseEvent) {
        if (userConfirmsRemovePhoto()) {
            changeProfilePhoto(DEFAULT_PROFILE_PHOTO_URL);
        }
    }

    /**
     * Saves the user's current first name if it was changed.
     */
    private void saveFirstName() {
        if (isFirstNameChanged()) {
            currentAccount.setFirstname(firstNameField.getText().strip());
        }
    }

    /**
     * Saves the user's current last name if it was changed.
     */
    private void saveLastName() {
        if (isLastNameChanged()) {
            currentAccount.setLastname(lastNameField.getText().strip());
        }
    }

    /**
     * Saves the user's current profile photo if it was changed.
     */
    private String saveProfilePhoto() throws Exception {
        if (profilePhotoChanged) {
            Image profileImage = getCurrentProfilePhoto();
            String imageURL = profileImage.getUrl();
            UploadImage imageUploader = new UploadImage(profileImage);

            if (!imageURL.equals(DEFAULT_PROFILE_PHOTO_URL)) {
                return imageUploader.upload();
            } else {
                currentAccount.setPicture(DEFAULT_PROFILE_PHOTO_URL);
            }
        }

        return null;
    }

    /**
     * Shows the user that the changes have been saved and asks if he/she wants to return to homepage.
     * If yes, the homepage will be loaded. Otherwise, the page will not be changed.
     */
    private void showSuccess() throws IOException {
        updateSidebar();
        profilePhotoChanged = false;

        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                CustomAlertType.SUCCESS,
                "Your changes have been saved! Return to homepage?",
                ButtonType.YES, ButtonType.NO
        );

        if (response.isPresent() && response.get() == ButtonType.YES) {
            Convenience.closePreviousDialog();
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

        if (imageURL.equals(DEFAULT_PROFILE_PHOTO_URL)) {
            removePhotoLabel.setDisable(true);
        } else {
            removePhotoLabel.setDisable(false);
        }

        profilePhotoChanged = true;
    }

    /**
     * Returns the current profile photo in the photo circle.
     *
     * @return {@link Image} object containing the current profile photo.
     */
    private Image getCurrentProfilePhoto() {
        ImagePattern imagePattern = (ImagePattern) profilePhotoCircle.getFill();
        return imagePattern.getImage();
    }

    /**
     * Pops up a dialog window to ask the user if he/she really wants to remove the profile photo.
     *
     * @return {@code true}, if the user presses the YES button. Otherwise, {@code false}.
     */
    private boolean userConfirmsRemovePhoto() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Are you sure you want to remove your profile picture?",
                ButtonType.YES, ButtonType.CANCEL
        );

        return response.isPresent() && response.get() == ButtonType.YES;
    }

    /**
     * Checks if the user's first name was changed.
     *
     * @return {@code true} if the first name was changed, otherwise {@code false}.
     */
    private boolean isFirstNameChanged() {
        String firstName = firstNameField.getText().strip();
        return !firstName.equals(currentAccount.getFirstname().strip());
    }

    /**
     * Checks if the user's last name was changed.
     *
     * @return {@code true} if the last name was changed, otherwise {@code false}.
     */
    private boolean isLastNameChanged() {
        String lastName = lastNameField.getText().strip();
        return !lastName.equals(currentAccount.getLastname().strip());
    }

    /**
     * Checks if the user's any settings (first/last name, profile photo) were changed.
     *
     * @return {@code true} if any settings were changed, otherwise {@code false}.
     */
    private boolean isAnythingChanged() {
        return isFirstNameChanged() ||
                isLastNameChanged() ||
                profilePhotoChanged;
    }

    /**
     * Updates the sidebar with the new settings a user specified.
     *
     * @throws IOException if the navbar/sidebar cannot be loaded
     */
    private void updateSidebar() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/navbar.fxml"));
        VBox mainUICenterVBox = (VBox) MainPane.getInstance().getBorderPane().getCenter();
        HBox navbarContainer = (HBox) mainUICenterVBox.getChildren().get(0);
        navbarContainer.getChildren().setAll((Node) loader.load());
    }
}
