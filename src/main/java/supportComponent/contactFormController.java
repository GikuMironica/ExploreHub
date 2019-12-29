package supportComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import handlers.MessageHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Account;


public class contactFormController {

    @FXML
    private BorderPane borderPane;
    Account account = CurrentAccountSingleton.getInstance().getAccount();
    @FXML
    private JFXTextArea text;
    @FXML
    private JFXButton sendButton;

    public void handleSendClicked(MouseEvent mouseEvent) {
        String body = text.getText();
        Thread thread = new Thread(() -> {
            MessageHandler messageHandler = MessageHandler.getMessageHandler();
            String email = account.getEmail();
            String name = account.getFirstname()+ " " + account.getLastname();
            String subject = "_" + email + "__" + name + "_Request for assistance";
            try{
                messageHandler.sendEmail(body,subject , "ExploreHub.help@gmail.com");
            }catch (Exception e){
                Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
            }
        });
        Convenience.closePreviousDialog();
       thread.start();

    }

    public void textTyped(){
        if(text.getText().trim().isEmpty()){
            sendButton.setDisable(true);
        }else {
            sendButton.setDisable(false);
        }
    }

}
