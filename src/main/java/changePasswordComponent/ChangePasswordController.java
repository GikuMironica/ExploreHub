package changePasswordComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import models.Account;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class which controls the "Change Password" view
 * @author Hidayat Rzayev
 */
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

    /**
     * Discards any changes and returns to the settings page.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleCancelClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/settings.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Applies the performed changes and updates the user's password in the database.
     * After updating the password, shows the dialog message to the user telling
     * that the password has been successfully changed.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleApplyClicked(MouseEvent mouseEvent) {
        String newPassword = newPasswordField.getText();

        EntityManager entityManager = currentAccount.getConnection();
        entityManager.getTransaction().begin();
        currentAccount.setPassword(newPassword);
        entityManager.getTransaction().commit();
        showSuccess();
    }

    /**
     * This method is invoked every time the user types in the "Current Password" field.
     * Checks if all the other fields are filled and if the corresponding passwords match.
     * If so, then the "Apply" button will be enabled. Otherwise, it will be disabled.
     *
     * @param keyEvent - the event which triggered the method
     */
    @FXML
    private void handleCurrentPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    /**
     * This method is invoked every time the user types in the "New Password" field.
     * Checks if all the other fields are filled and if the corresponding passwords match.
     * If so, then the "Apply" button will be enabled. Otherwise, it will be disabled.
     *
     * @param keyEvent - the event which triggered the method.
     */
    @FXML
    private void handleNewPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    /**
     * This method is invoked every time the user types in the "Confirm New Password" field.
     * Checks if all the other fields are filled and if the corresponding passwords match.
     * If so, then the "Apply" button will be enabled. Otherwise, it will be disabled.
     *
     * @param keyEvent - the event which triggered the method.
     */
    @FXML
    private void handleConfirmPasswordChanged(KeyEvent keyEvent) {
        if (allFieldsFilled() && passwordsMatch()) {
            applyButton.setDisable(false);
        } else {
            applyButton.setDisable(true);
        }
    }

    /**
     * Checks if the corresponding passwords match.
     * Namely:
     *  1. If the user's current password indeed matches with the password typed in the "Current Password" field.
     *  2. If the new password matches with the password typed in the "Confirm New Password" field.
     *
     * @return {@code true}, if both of the above conditions are met. Otherwise, {@code false}.
     */
    private boolean passwordsMatch() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        return currentPassword.equals(currentAccount.getPassword()) &&
                newPassword.equals(confirmPassword);
    }

    /**
     * Checks if all the password fields are non-empty.
     *
     * @return {@code true}, if all the fields are non-empty. Otherwise, {@code false}.
     */
    private boolean allFieldsFilled() {
        return !currentPasswordField.getText().isEmpty() &&
                !newPasswordField.getText().isEmpty() &&
                !confirmPasswordField.getText().isEmpty();
    }

    /**
     * Shows the dialog message to the user saying that the password has been successfully changed.
     * Once the user clicks the "OK" button, the homepage will be loaded.
     */
    private void showSuccess() {
        Convenience.showAlert(Alert.AlertType.INFORMATION,
                "Information", "Password changed successfully", "Press OK to continue");

        try {
            Convenience.switchScene(applyButton, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }
}
