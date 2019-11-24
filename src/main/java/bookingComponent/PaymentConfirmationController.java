package bookingComponent;

import handlers.Convenience;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentConfirmationController implements Initializable {

    @FXML
    private Label confirmationText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            confirmationText.setText(PaymentController.getConfirmationText());
        }catch(NullPointerException e){e.printStackTrace();}
    }

    @FXML
    public void goToHomepage(Event event){
        try {
            Convenience.switchScene(event, getClass().getResource("/FXML/mainUI.fxml"));
        }catch(IOException e){e.printStackTrace();}
    }

    //public static void setConfirmationText(Label text){confirmationText.setText(text);}
    public synchronized Label getConfirmationText(){return confirmationText;}

}
