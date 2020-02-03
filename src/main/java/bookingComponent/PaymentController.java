package bookingComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import listComponent.EventListSingleton;
import mainUI.MainPane;
import models.Events;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Payment Controller
 * @author Domagoj Frecko
 */

public class PaymentController implements Initializable {
    @FXML
    Pane container;

    @FXML
    Button payBtn, cancel, back;

    @FXML
    Label totalPrice;

    Pane newPane;
    private boolean cardInfo = true;
    private PaymentCardController paymentCardController;

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
            } catch (IOException e){e.printStackTrace();}

        }

        // Card
        else if(BookingController.getPaymentType() == 0){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXML/paymentCard.fxml"));
                newPane = fxmlLoader.load();
                paymentCardController = (PaymentCardController) fxmlLoader.getController();
                container.getChildren().add(newPane);
            } catch (IOException e){e.printStackTrace();}
        }

        // Free
        else if(BookingController.getPaymentType() == 2){
            Platform.runLater(()->{
                //Convenience.closePreviousDialog();
                payment();
            });
        }

    }

    /**
     * Method which initiates the payment strategy and starts the booking process.
     */
    @FXML
    public void payment(){ // Once user presses Pay button initiate strategy
        if (BookingController.getPaymentType() == 0){
            cardInfo = checkInfo();
        }

        if (cardInfo) {
            disableControlBtns();
            totalPrice.setVisible(false);
            boolean isBooked = false;

            if (BookingController.getPaymentType() == 1) {
                CashPaymentStrategy CashPS = new CashPaymentStrategy();
                isBooked = CashPS.pay();
                confirmationText = "Booking successful.\nPayment with: Cash\nVisit Prittwitzstrasse Campus to pay and get approved.";
            } else if (BookingController.getPaymentType() == 0) {
                CardPaymentStrategy CardPS = new CardPaymentStrategy();
                isBooked = CardPS.pay();
                confirmationText = "Booking successful.\nPayment with: Card";
            } else if (BookingController.getPaymentType() == 2) {
                FreePaymentStrategy FreePS = new FreePaymentStrategy();
                isBooked = FreePS.pay();
                confirmationText = "Booking successful.\nEnjoy your trip!";
            }

            if (isBooked) {
                Convenience.closePreviousDialog();
                confirmationScene();
                EventListSingleton.getInstance().refreshList();
            }
        }
    }

    /**
     * Method which goes back to the payment selection screen
     * @param event triggered on button ('Back') press
     */
    @FXML
    public void goBack(Event event){
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/booking.fxml"));
        }catch(IOException e){
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
        }
    }

    /**
     * Method which cancels the booking and returns the user to the homepage
     * @param event triggered on button ('Cancel') press
     */
    @FXML
    public void cancelBooking(Event event){ // Once user presses Cancel button - cancel the booking
        BookingController.setPaymentTypeValue(100);
        Convenience.closePreviousDialog();
    }

    /**
     * Method which displays the confirmation screen once booking is successful
     */
    public void confirmationScene(){
        //try {
            //Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
            //        getClass().getResource("/FXML/paymentConfirmation.fxml"));
            Convenience.showAlert(CustomAlertType.SUCCESS, confirmationText);
       // } catch (IOException e) {
       //     Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
       // }
    }

    public void disablePayBtn(){
        Platform.runLater(()->{
            payBtn.setDisable(true);
            payBtn.setVisible(false);
        });
    }

    /**
     * Method which disables the control buttons on the screen
     */
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

    /**
     * Method which checks whether the card information has been entered
     * @return true if card info has been entered
     */
    public boolean checkInfo(){
        boolean infoMissing = false;
        if(paymentCardController.getIban() == null || paymentCardController.getIban().isEmpty()){ infoMissing = true; }
        if(paymentCardController.getBic() == null || paymentCardController.getBic().isEmpty()){ infoMissing = true; }
        if(paymentCardController.getPin() == null || paymentCardController.getPin().isEmpty()){ infoMissing = true; }

        if(infoMissing){
            Convenience.showAlert(CustomAlertType.WARNING, "Please fill out the fields with the correct values.");
        }

        return !infoMissing;
    }

}
