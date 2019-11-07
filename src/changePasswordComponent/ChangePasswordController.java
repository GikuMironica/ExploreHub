package changePasswordComponent;

import authentification.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Account;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button applyButton;

    private Account currentAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
    }

    @FXML
    private void handleCancelClicked(MouseEvent mouseEvent) {
        try {
            switchScene("/settingsComponent/settings.fxml");
        } catch (IOException e) {
            System.out.println("Exception occured: " + e.getMessage());
        }
    }

    @FXML
    private void handleApplyClicked(MouseEvent mouseEvent) {
        String newPassword = newPasswordField.getText();

        EntityManager entityManager = currentAccount.getConnection();
        entityManager.getTransaction().begin();
        currentAccount.setPassword(newPassword);
        entityManager.getTransaction().commit();
        showSuccess();
    }

    @FXML
    private void handleCurrentPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    @FXML
    private void handleNewPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    @FXML
    private void handleConfirmPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    private boolean passwordsMatch() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        return currentPassword.equals(currentAccount.getPassword()) &&
                newPassword.equals(confirmPassword);
    }

    private boolean allFieldsFilled() {
        return !currentPasswordField.getText().isEmpty() &&
                !newPasswordField.getText().isEmpty() &&
                !confirmPasswordField.getText().isEmpty();
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Password changed successfully!");
        alert.setContentText("Press OK to continue");
        alert.showAndWait();

        try {
            switchScene("/mainUI/mainUI.fxml");
        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private void switchScene(String resourcePath) throws  IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourcePath));
        Scene scene = new Scene(root);
        Stage window = (Stage) applyButton.getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
