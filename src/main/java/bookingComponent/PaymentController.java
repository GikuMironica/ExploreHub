package bookingComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import mainUI.MainPane;
import models.Events;
import models.User;

import java.awt.print.Book;
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
    Button payBtn, cancel, back;

    @FXML
    Label totalPrice;

    Pane newPane;

    static String confirmationText;

    private List<Events> evList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        evList = CurrentAccountSingleton.getInstance().getAccount().getBookedEvents();

        totalPrice.setText("Total: €" + BookingController.getTotalPrice());


        // Cash
        if(BookingController.getPaymentType() == 1){

            try{
                newPane = FXMLLoader.load(getClass().getResource("/FXML/paymentCash.fxml"));
                container.getChildren().add(newPane);
            }catch (IOException e){e.printStackTrace();}

        }

        // Card
        else if(BookingController.getPaymentType() == 0){
            try{
                newPane = FXMLLoader.load(getClass().getResource("/FXML/paymentCard.fxml"));
                container.getChildren().add(newPane);
            }catch (IOException e){e.printStackTrace();}
        }

        // Free
        else if(BookingController.getPaymentType() == 2){
            payment();
        }

    }

    @FXML
    public void payment(){ // Once user presses Pay button initiate strategy

        disableControlBtns();
        totalPrice.setVisible(false);

        if(BookingController.getPaymentType() == 1){
            CashPaymentStrategy CashPS = new CashPaymentStrategy();
            CashPS.pay();
            confirmationText = "Booking successful! Visit Prittwitzstrasse campus to pay and get approved.\nWe hope you have a nice TimeConvertor!";
        }
        else if (BookingController.getPaymentType() == 0 ) {
            CardPaymentStrategy CardPS = new CardPaymentStrategy();
            CardPS.pay();
            confirmationText = "Payment with card successful.\nWe hope you have a nice TimeConvertor!";
        }
        else if (BookingController.getPaymentType() == 2){
            FreePaymentStrategy FreePS = new FreePaymentStrategy();
            FreePS.pay();
            confirmationText = "Free booking successful.\nEnjoy your trip!";
        }

        confirmationScene();

    }

    @FXML
    public void goBack(Event event){
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/booking.fxml"));
        }catch(IOException e){
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    @FXML
    public void cancelBooking(Event event){ // Once user presses Cancel button - cancel the booking
        BookingController.setPaymentTypeValue(100);
        Convenience.closePreviousDialog();
    }

    public void confirmationScene(){
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/paymentConfirmation.fxml"));
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    public void disablePayBtn(){
        Platform.runLater(()->{
            payBtn.setDisable(true);
            payBtn.setVisible(false);
        });
    }

    public void disableControlBtns(){
        Platform.runLater(()->{
            payBtn.setDisable(true);
            payBtn.setVisible(false);
            cancel.setDisable(true);
            cancel.setVisible(false);
            back.setDisable(true);
            back.setVisible(false);

        });
    }

    public static String getConfirmationText(){return confirmationText;}
}
