package authentification;

import handlers.Convenience;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 *Class that controls the password recovery view.
 * @author Aleksejs Marmiss
 *
 */
public class RecoverController implements Initializable {
    @FXML
    private TextField confirmationCode;
    @FXML
    private TextField recoveryEmail;
    @FXML
    private Button confirmationCodeButton;
    @FXML
    private Button sendEmailButton;
    @FXML
    private Label notifyUser;
    private String generatedKey = null;
    private TypedQuery<User> checkUserQuery;
    private List<User> users;
    private GuestConnectionSingleton connection;
    private EntityManager entityManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recoveryEmail.setPromptText("Please enter your email.");
        confirmationCode.setDisable(true);
        confirmationCodeButton.setDisable(true);
    }

    /**
     * Method that generates confirmation code for password recovery.
     * Initialized by button
     * @param event button pressed event.
     */
    @FXML
    private void generateConfirmCode(Event event){
        recoveryEmail.setDisable(true);
        sendEmailButton.setDisable(true);
        Thread thread = new Thread(() -> {
            if (checkUser(recoveryEmail.getText())) {
                String key = UUID.randomUUID().toString();
                generatedKey = key;
                MessageHandler messageHandler = MessageHandler.getMessageHandler();
                try {
                    messageHandler.sendRecoveryConfirmation(key, recoveryEmail.getText());
                } catch (MessagingException ade) {
                    Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.WARNING,
                                            "Warning", "Attempt failed.", "Password recovery failed. Please try again later."));
                    ade.printStackTrace();
                }
                setConfirmEnabled();
            }else{
                Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.ERROR,
                                        "Error", "Attempt failed.", "User does not exist"));
                setMailEnabled();
            }
        });
        thread.start();
    }

    /**
     *Method that performs check of confirmation code and generates new password.
     * Initialized by button
     * @param event button pressed event.
     * @throws IOException In case if failed to send a message.
     */
    @FXML
    private void confirmButton(Event event){
        confirmationCode.setDisable(true);
        confirmationCodeButton.setDisable(true);
        String userKey = confirmationCode.getText();
        Thread thread = new Thread(() -> {
            if (generatedKey != null && generatedKey.equals(userKey)){
                String generatedPassword = generatePassword();
                setNewPassword(generatedPassword);
                try {
                    MessageHandler messageHandler = MessageHandler.getMessageHandler();
                    messageHandler.sendNewPassword(generatedPassword, recoveryEmail.getText());
                }catch (MessagingException me){
                    Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.WARNING,
                                                "Warning", "Attempt failed.", "Password recovery failed. Please try again later." ));
                    me.printStackTrace();
                }
                Platform.runLater(() -> Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.INFORMATION,
                                                                    "Attention", "Password recovery", "Your new password was sent on your email.")));
                generatedKey = null;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Convenience.switchScene(event, getClass().getResource("/authentification/authentification.fxml"));
                        }catch (IOException ioe){
                            Convenience.showAlert(Alert.AlertType.ERROR,
                                    "Error", "Something went wrong", "Please, try again later");
                        }
                    }
                });

            }else{
                setEisabled();
            }
        });
        thread.start();
    }

    /**
     *Method that checks if the user with given email exists
     * @param email user email as a String.
     * @return  true if user exists and false if does not exist.
     */
    private boolean checkUser(String email){
        connection = GuestConnectionSingleton.getInstance();
        entityManager = connection.getManager();
        checkUserQuery = entityManager.createNamedQuery(
                "User.findUserbyEmail",
                User.class);
        checkUserQuery.setParameter("email", email);
        users = checkUserQuery.getResultList();
        return users.size() == 1;
    }

    /**
     *Method that saves newly generated password in the database.
     * @param newPassword new password as a String.
     */
    private void setNewPassword(String newPassword){
        User user = users.get(0);
        user.setPassword(newPassword);
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }

    /**
     *Method that generates new password.
     * @return password as a String.
     */
    private String generatePassword(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

            Random random = new Random();
            StringBuilder builder = new StringBuilder(10);
            for (int digitPos = 0; digitPos < 10; digitPos++) {
                builder.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            return builder.toString();
    }

    /**
     * Method that enables UI fields using main thread.
     */
    private void setMailEnabled(){
        Platform.runLater(() -> {
            recoveryEmail.setDisable(false);
            sendEmailButton.setDisable(false);
        });

    }

    /**
     * Method that enables UI fields using main thread.
     */
    private void setConfirmEnabled(){
        Platform.runLater(() -> {
            confirmationCode.setDisable(false);
            confirmationCodeButton.setDisable(false);
            notifyUser.setText("Confirmation code has been sent on your email.");
        });

    }

    /**
     * Method that enables UI fields using main thread.
     */
    private void setEisabled(){
        Platform.runLater(() -> {
            confirmationCode.setDisable(false);
            confirmationCodeButton.setDisable(false);
            notifyUser.setText("Wrong confirmation code");
        });

    }
}
