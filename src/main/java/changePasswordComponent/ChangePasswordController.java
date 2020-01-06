package changePasswordComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import handlers.Convenience;
import javafx.scene.control.TextFormatter;
import mainUI.MainPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private JFXPasswordField currentPasswordField;

    @FXML
    private JFXPasswordField newPasswordField;

    @FXML
    private JFXPasswordField confirmPasswordField;

    @FXML
    private JFXButton applyButton;

    private Account currentAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        initPasswordFields();
    }

    /**
     * Initializes the password fields.
     */
    private void initPasswordFields() {
        currentPasswordField.requestFocus();

        currentPasswordField.setTextFormatter(ignoreSpace());
        confirmPasswordField.setTextFormatter(ignoreSpace());
        newPasswordField.setTextFormatter(ignoreSpace());
    }

    /**
     * Creates and returns a new text formatter that consumes space characters.
     *
     * @return {@link TextFormatter} object.
     */
    private TextFormatter<String> ignoreSpace() {
        return new TextFormatter<>(change -> {
            if (change.getText().isBlank()) {
                change.setText("");
            }
            return change;
        });
    }

    /**
     * Discards any changes and returns to the settings page.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleCancelClicked(MouseEvent mouseEvent) {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/settings.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
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
        if (newPasswordEqualsCurrentPassword()) {
            Convenience.showAlert(CustomAlertType.WARNING, "A new password cannot be the same as the current password.");
            return;
        }

        String newPassword = newPasswordField.getText();

        EntityManager entityManager = currentAccount.getConnection();
        entityManager.getTransaction().begin();
        currentAccount.setPassword(newPassword);
        entityManager.getTransaction().commit();
        showSuccess(mouseEvent);
    }

    /**
     * This method is invoked every TimeConvertor the user types in the "Current Password" field.
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
     * This method is invoked every TimeConvertor the user types in the "New Password" field.
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
     * This method is invoked every TimeConvertor the user types in the "Confirm New Password" field.
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
     * Once the user clicks the "OK" button, the settings page will be loaded.
     */
    private void showSuccess(MouseEvent mouseEvent) {
        Convenience.showAlert(CustomAlertType.SUCCESS, "Your password has been successfully changed!");

        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/settings.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
        }
    }

    /**
     * Checks if the new password equals the user's current password.
     *
     * @return {@code true} if the passwords are equal, otherwise {@code false}
     */
    private boolean newPasswordEqualsCurrentPassword() {
        String newPassword = newPasswordField.getText();
        return newPassword.equals(currentAccount.getPassword());
    }
}
