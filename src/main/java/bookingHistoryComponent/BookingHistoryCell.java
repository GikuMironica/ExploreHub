package bookingHistoryComponent;

import alerts.CustomAlertType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import controlPanelComponent.PaymentMethod;
import controlPanelComponent.TransactionStatus;
import handlers.CacheSingleton;
import handlers.Convenience;
import handlers.HandleNet;
import handlers.MessageHandler;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import mainUI.MainPane;
import models.Events;
import models.Transactions;

import java.io.IOException;

/**
 * A class which reflects exactly one Entity -> Transaction
 * @author Gheorghe Mironica
 */
public class BookingHistoryCell  extends JFXListCell<Transactions> {

    private FXMLLoader loader;
    private Events bookedEvent;
    private Transactions currentTransaction;

    @FXML
    private AnchorPane bookingHistoryAPane;

    @FXML
    private ImageView eventLogoImage;

    @FXML
    private Label locationLabel, priceLabel, dateLabel, statusLabel, titleLabel;

    @FXML
    private JFXButton requestButton;

    private String REQUEST_MESSAGE;
    private String REQUEST_SUBJECT = "TRANSACTION_CANCEL_REQUEST";
    private final String REQUEST_RECIPIENT = "ExploreHub.help@gmail.com";

    @Override
    protected void updateItem(Transactions transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/FXML/bookinghistoryCell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException ioe) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            }

            currentTransaction = transaction;
            bookedEvent = transaction.getEvent();
            fillCell(transaction);
        }
    }

    /**
     * This method parses the incoming {@link Transactions} object
     * and fills the view with the information
     * @param transaction {@link Transactions} transaction input parameter
     */
    private void fillCell(Transactions transaction){

        CacheSingleton cache = CacheSingleton.getInstance();
        int id = bookedEvent.getId();

        Image eventImage;
        if (cache.containsImage(id)) {
            eventImage = cache.getImage(id);
        } else {
            String _url = bookedEvent.getPicture().getLogo();
            eventImage = new Image(_url);
            cache.putImage(id, eventImage);
        }

        eventLogoImage.setImage(eventImage);
        locationLabel.setText(bookedEvent.getLocation().getCity());
        dateLabel.setText(String.valueOf(bookedEvent.getDate()));
        priceLabel.setText(String.valueOf(bookedEvent.getPrice()));
        titleLabel.setText(bookedEvent.getShortDescription());

        String status = String.valueOf(TransactionStatus.valueOf(transaction.getCompleted()));
        statusLabel.setText(status);
        if(transaction.getCompleted()== 3  || transaction.getCompleted() == 2 || transaction.getRequested() == 1)
            requestButton.setDisable(true);

        setText(null);
        setGraphic(bookingHistoryAPane);
    }

    /**
     * This method delegates the responsibility to send an
     * email request to the Message Handler with the
     * data about the transaction to be canceled
     * The email is sent in parallel task
     * @param event {@link Event} method trigger
     */
    @FXML
    private void sendRequest(Event event){
        currentTransaction.setRequested(1);
        requestButton.setDisable(true);
        String paymentMethod = String.valueOf(PaymentMethod.valueOf(currentTransaction.getPaymentMethod()));

        MessageHandler messageHandler = MessageHandler.getMessageHandler();
        REQUEST_MESSAGE = "TransactionID : "+currentTransaction.getId()+"\nTransaction Date : "+currentTransaction.getDate()+"\nEventID : "+bookedEvent.getId()+
                "\nStudent Name : "+currentTransaction.getUser().getFirstname()+" "+currentTransaction.getUser().getLastname()+"\nPayment Method : "+paymentMethod+
                "\nPrice : "+currentTransaction.getEvent().getPrice();

        try {
            Thread t1 = new Thread(() -> {
                try {
                    messageHandler.sendEmail(REQUEST_MESSAGE, "_"+currentTransaction.getUser().getEmail()+"_"+REQUEST_SUBJECT, REQUEST_RECIPIENT);
                }catch(Exception e){
                    // email
                }
            });
        t1.start();
        }catch(Exception e){
            handleConnection();
        }
    }

    /**
     * This method handles the loss of internet connection
     * delegating it to NoInternet controller
     */
    private void handleConnection(){
            try {
                if(!HandleNet.hasNetConnection()) {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                }else {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            }catch(Exception e) { /**/ }
    }
}

