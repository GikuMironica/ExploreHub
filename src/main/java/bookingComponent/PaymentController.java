package bookingComponent;

import authentification.CurrentAccountSingleton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import models.Events;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;



public class PaymentController implements Initializable {
    @FXML
    Pane container;

    @FXML
    Button payBtn;

    @FXML
    Label totalPrice;

    Pane newPane;

    static String confirmationText;

    private List<Events> evList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        evList = ((User) (CurrentAccountSingleton.getInstance().getAccount())).getBookedEvents();

        totalPrice.setText("Total: " + BookingController.getTotalPrice());


        // Cash
        if(BookingController.getPaymentType() == 0){

            // Final text confirming cash purchase

        }

        // Card
        else if(BookingController.getPaymentType() == 1){
            try{
                newPane = FXMLLoader.load(getClass().getResource("/FXML/paymentCard.fxml"));
                container.getChildren().add(newPane);
            }catch (IOException e){e.printStackTrace();}
        }

    }

    @FXML
    public void payment(){ // Once user presses Pay button initiate strategy

        disablePayBtn();

        if(BookingController.getPaymentType() == 0){
            CashPaymentStrategy CashPS = new CashPaymentStrategy();
            CashPS.pay();
            confirmationText = "Payment with Cash. Visit X at Y to pay.";
        }
        else if (BookingController.getPaymentType() == 1 ) {
            CardPaymentStrategy CardPS = new CardPaymentStrategy();
            CardPS.pay();
            confirmationText = "Payment with card successful.";
        }

        confirmationScene();

    }

    public void confirmationScene(){
        try {
            container.getChildren().clear();
            newPane = FXMLLoader.load(getClass().getResource("/FXML/paymentConfirmation.fxml"));
            container.getChildren().add(newPane);
        }catch (IOException e){e.printStackTrace();}
    }

    public void disablePayBtn(){
        Platform.runLater(()->{
            payBtn.setDisable(true);
            payBtn.setVisible(false);
        });
    }

    public static String getConfirmationText(){return confirmationText;}
}
