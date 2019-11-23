package bookingComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import models.Events;
import models.Transactions;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Booking Controller
 * @author Domagoj Frecko
 */

public class BookingController implements Initializable {
    @FXML
    private ToggleGroup toggleGroupPayments;

    @FXML
    Label totalPrice;

    private RadioButton selectedRadioBtn;
    private String toggleValue;
    private static int paymentType;

    private List<Events> evList;
    private Events tempEvent;
    private static int total = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Get events from basket and sum the prices for total price
        evList = ((User) (CurrentAccountSingleton.getInstance().getAccount())).getBookedEvents();

        if(evList != null) {
            ListIterator iterator = evList.listIterator();
            while (iterator.hasNext()) {
                tempEvent = (Events) iterator.next();
                total += tempEvent.getPrice();
            }
        }
        totalPrice.setText("Total: " + total);
    }

    /**
     * Method that starts the payment process. (User selected a payment method)
     * @param event triggered on button ('Next') press
     */
    @FXML
    private void proceedToPayment(Event event) {
        // Once user presses next go to Payment FXML window (must pass the paymentType to load the right component!)
        try {
            Convenience.switchScene(event, getClass().getResource("/FXML/payment.fxml"));
        } catch (IOException e){e.printStackTrace();}
    }

    /**
     * Method which is used to select a payment
     * @param event
     */
    @FXML
    private void selectPayment(Event event){
        selectedRadioBtn = (RadioButton) toggleGroupPayments.getSelectedToggle();
        toggleValue = selectedRadioBtn.getText().toLowerCase();

        setPaymentType(toggleValue);
    }

    /**
     * Method which returns the user to the homepage
     * @param event triggered on button ('Cancel') press
     */
    @FXML
    private void cancelBooking(Event event){
        // User changed their mind and pressed the Cancel button
        try {
            Convenience.switchScene(event, getClass().getResource("/FXML/mainUI.fxml"));
        }catch(IOException e){e.printStackTrace();}

    }

    // GETTERS AND SETTERS

    /**
     * Method that sets the payment method which the user wants to use
     * @param payment Temporary paramater
     */
    public static void setPaymentType(String payment){
        if(payment.equals("cash")) paymentType = 0;
        else if (payment.equals("card")) paymentType = 1;
    }
    public static int getPaymentType(){return paymentType;}

    public static int getTotalPrice(){return total;}

}
