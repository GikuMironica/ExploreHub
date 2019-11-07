package settingsComponent;

import authentification.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            switchScene("/changePasswordComponent/change_password.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
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

        Stage window = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Unsaved changes will be lost");
        alert.setContentText("Do you want to continue?");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                switchScene("/mainUI/mainUI.fxml");
            } catch (IOException e) {
                System.out.println("Exception occurred: " + e.getMessage());
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
     * Switches the current scene to the given one
     *
     * @param resourcePath - path to fxml file which is to be loaded
     * @throws IOException - can be thrown if the page could not be loaded
     */
    private void switchScene(String resourcePath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
        Scene scene = new Scene(root);
        Stage window = (Stage) profilePhotoCircle.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Shows the user that the changes have been saved and asks if he/she wants to return to homepage.
     * If yes, the homepage will be loaded. Otherwise, the page will not be changed.
     */
    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Your changes have been saved!");
        alert.setContentText("Return to homepage?");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CLOSE);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                switchScene("/mainUI/mainUI.fxml");
            } catch (IOException e) {
                System.out.println("Exception occurred: " + e.getMessage());
            }
        }
    }
}
