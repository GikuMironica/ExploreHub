package bookingComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mainUI.MainPane;
import models.Events;

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
    Label totalPrice, bookingDescription;

    @FXML
    ImageView bookingImage;

    @FXML
    RadioButton cash, card, free;

    private RadioButton selectedRadioBtn;
    private String toggleValue;
    private static int paymentType;

    private List<Events> evList;
    private Events tempEvent;
    private static int total;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentType = 1;
        free.setDisable(true);
        free.setVisible(false);

        // Sum the prices for total price
        evList = CurrentAccountSingleton.getInstance().getAccount().getBookedEvents();
        total = 0;

        if (evList != null) {
            ListIterator iterator = evList.listIterator();
            while (iterator.hasNext()) {
                tempEvent = (Events) iterator.next();
                total += tempEvent.getPrice();
            }
        }

        if (total == 0) { // Events are free lock into free option
            paymentType = 2;
        }

        switch (getPaymentType()) {
            case 1:
                cash.setSelected(true);
                paymentType = 1;
                break;

            case 0:
                card.setSelected(true);
                break;

            case 2:
                free.setDisable(false);
                free.setVisible(true);
                free.setSelected(true);

                paymentType = 2;

                cash.setDisable(true);
                card.setDisable(true);
                totalPrice.setVisible(false);
                break;

            default:
                cash.setSelected(true);
                paymentType = 1;
        }


        if(total != 0) totalPrice.setText("Total: €" + total);

        Image image = new Image(evList.get(0).getPicture().getPicture());
        if(image.isError()){
            image = new Image(getClass().getResourceAsStream("/IMG/quest.png"));
        }
        if (evList.size() > 1) {
            try {
                bookingImage.setImage(image);
                bookingDescription.setText(evList.get(0).getShortDescription() + " And " + (evList.size() - 1) + " more event(s)...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                bookingImage.setImage(image);
                bookingDescription.setText(evList.get(0).getShortDescription());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method that starts the payment process. (User selected a payment method)
     * @param event triggered on button ('Next') press
     */
    @FXML
    private void proceedToPayment(Event event) {
        // Once user presses next go to Payment FXML window (must pass the paymentType to load the right component!)
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/payment.fxml"));
        } catch (IOException e){
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
        }
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
     * Method which cancels the booking and returns the user to the homepage
     * @param event triggered on button ('Cancel') press
     */
    @FXML
    private void cancelBooking(Event event){
        // User changed their mind and pressed the Cancel button
        setPaymentTypeValue(100);
        Convenience.closePreviousDialog();

    }

    // GETTERS AND SETTERS

    /**
     * Method that sets the payment method which the user wants to use
     * @param payment Temporary parameter
     */
    public static void setPaymentType(String payment){
        if(payment.equals("cash")) paymentType = 1;
        else if (payment.equals("card")) paymentType = 0;
    }
    public static void setPaymentTypeValue(int val){paymentType = val;}

    public static int getPaymentType(){return paymentType;}

    public static int getTotalPrice(){return total;}

}
