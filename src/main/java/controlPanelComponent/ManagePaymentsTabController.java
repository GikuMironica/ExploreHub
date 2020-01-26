package controlPanelComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import handlers.HandleNet;
import handlers.MessageHandler;
import handlers.Convenience;
import handlers.GeneratePDF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import mainUI.MainPane;
import models.Events;
import models.Invoice;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Controller class for the managePaymentsTab view
 *
 * @author Gheorghe Mironica
 */
public class ManagePaymentsTabController {

    @FXML
    private Label paymethodLabel, companyLabel, transactionLabel, eventidLabel, eventnameLabel, eventdateLabel, priceLabel, studentnameLabel, studentemailLabel, statusLabel, homeLabel;
    @FXML
    private Button validateButton, rejectButton, refundButton;
    @FXML
    private RadioButton radioAll, radioOpen, radioProcessed;
    @FXML
    private ListView<Transactions> transactionsListView;

    private Transactions selectedTransaction;
    private ObservableList<Transactions> transactionsList;
    private User selectedUser;
    private Events selectedEvent;
    private EntityManager entityManager;
    private MessageHandler messageHandler;


    /**
     * This method initializes the views
     *
     */

    public void initialize() {
        // no button until transaction selected
        validateButton.setDisable(true);
        rejectButton.setDisable(true);
        refundButton.setDisable(true);

        // get connection
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();

        // initialize listview
        transactionsList = FXCollections.observableArrayList();
        transactionsListView.setCellFactory((editListView) -> new TransactionsListViewCell());
        allRadioEnabled();
        transactionsListView.setItems(transactionsList);

        messageHandler = MessageHandler.getMessageHandler();
    }

    /**
     * This method handles the listview cell click
     *
     * @param event method trigger {@link Event}
     */
    @FXML
    private void cellClicked(Event event){
        try {
            selectedTransaction = transactionsListView.getSelectionModel().getSelectedItem();
            entityManager.refresh(selectedTransaction);
            disableButtons(selectedTransaction);
            selectedUser = selectedTransaction.getUser();
            selectedEvent = selectedTransaction.getEvent();

            // fill the form with selected data
            fillTransactionForm();
        } catch(Exception e){
            handleConnection();
        }
    }

    /**
     * This method selects all transactions from database, displays them in the listview
     *
     */
    @FXML
    private void allRadioEnabled(){
        radioAll.setSelected(true);
        radioOpen.setSelected(false);
        radioProcessed.setSelected(false);

        try {
            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Transactions> tQuery = entityManager.createNamedQuery("Transactions.findAllTransactions", Transactions.class);
            transactionsList.clear();
            transactionsList.addAll(tQuery.getResultList());
        }catch(Exception e){
            handleConnection();
        }
    }

    /**
     * This method selects only open transactions from database, displays them in the listview
     *
     */
    @FXML
    private void openRadioEnabled(){
        radioOpen.setSelected(true);
        radioAll.setSelected(false);
        radioProcessed.setSelected(false);

        try {
            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Transactions> tQuery = entityManager.createNamedQuery("Transactions.findAllActiveTransactions", Transactions.class);
            transactionsList.clear();
            transactionsList.addAll(tQuery.getResultList());
        }catch(Exception e){
            handleConnection();
        }
    }

    /**
     * This method selects only processes transactions from database, displays them in the listview
     *
     */
    @FXML
    private void processedRadioEnabled(){
        radioProcessed.setSelected(true);
        radioAll.setSelected(false);
        radioOpen.setSelected(false);

        try {
            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Transactions> tQuery = entityManager.createNamedQuery("Transactions.findAllProcessedTransactions", Transactions.class);
            transactionsList.clear();
            transactionsList.addAll(tQuery.getResultList());
        }catch(Exception e){
            handleConnection();
        }
    }

    /**
     * This method fills the form with the data from selected transaction
     */
    protected void fillTransactionForm() {
        transactionLabel.setText(String.valueOf(selectedTransaction.getId()));
        eventidLabel.setText(String.valueOf(selectedEvent.getId()));
        eventnameLabel.setText(String.valueOf(selectedEvent.getShortDescription()));
        eventdateLabel.setText(String.valueOf(selectedEvent.getDate()));
        priceLabel.setText(String.valueOf(selectedEvent.getPrice()));
        studentnameLabel.setText(selectedUser.getFirstname()+" "+selectedUser.getLastname());
        studentemailLabel.setText(selectedUser.getEmail());
        companyLabel.setText(selectedEvent.getCompany());

        // fill for the transaction transactionStatus
        TransactionStatus transactionStatus = TransactionStatus.valueOf(selectedTransaction.getCompleted());
        statusLabel.setText(String.valueOf(transactionStatus));

        // fill for payment method
        PaymentMethod payMethod = PaymentMethod.valueOf(selectedTransaction.getPaymentMethod());
        paymethodLabel.setText(String.valueOf(payMethod));
    }

    /**
     * This method validates the selected transaction
     *
     * @param event method trigger {@link Event}
     */
    @FXML
    private void validatePayment(Event event){
        Optional<ButtonType> response = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                "Are you sure you want to validate this transaction?", ButtonType.YES, ButtonType.CANCEL);

        if(response.isPresent() && response.get() == ButtonType.CANCEL){
            return;
        } else {
            String message= "The payment for one of your booked event has been approved";
            selectedTransaction.setCompleted(1);
            try {
                Invoice invoice = new Invoice(selectedTransaction);
                Events bookedEvent = selectedTransaction.getEvent();
                int places = bookedEvent.getAvailablePlaces();
                places--;
                bookedEvent.setAvailablePlaces(places);
                entityManager.getTransaction().begin();
                selectedTransaction.setInvoice(invoice);
                entityManager.merge(selectedTransaction);
                entityManager.merge(bookedEvent);
                entityManager.getTransaction().commit();
            }catch(Exception exc){
                handleConnection();
                return;
            }
            GeneratePDF pdf;
            String newFile = "";
            try {
                pdf = new GeneratePDF(selectedUser, selectedTransaction);
                newFile = pdf.getFilename();
                messageHandler.sendConfirmation(message, selectedUser.getEmail(), newFile);
            }catch(Exception e){
               System.out.println("Invalid Email");
            }

            try{
                Path fileToDeletePath = Paths.get(newFile);
                Files.delete(fileToDeletePath);
                clearView();
            } catch(Exception e){
            }

            openRadioEnabled();
        }
    }


    /**
     * This method rejects the selected transaction
     *
     * @param event method trigger {@link Event}
     */
    @FXML
    private void rejectPayment(Event event){
        Optional<ButtonType> response = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                "Are you sure you want to reject this transaction?", ButtonType.YES, ButtonType.CANCEL);

        if(response.isPresent() && response.get() == ButtonType.CANCEL){
            return;
        } else {
            openRadioEnabled();
            try {
                String message= "The payment for one of your booked event has been rejected";
                startTransaction(2);
                messageHandler.sendConfirmation(message, selectedUser.getEmail());
                clearView();
            } catch (Exception e) {
               System.out.println("Email doesn't exist");
            }
        }
    }


    /**
     * This method cancels the selected, already processed transaction
     *
     * @param event method trigger {@link Event}
     */
    @FXML
    private void refundTransaction(Event event){

        Optional<ButtonType> response = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                "Are you sure you want to undo this transaction?", ButtonType.YES, ButtonType.CANCEL);

        if(response.isPresent() && response.get() == ButtonType.CANCEL){
            return;
        } else {
            openRadioEnabled();

            try {
                String message= "The payment for one of your booked event has been refunded";
                startTransaction(3);
                messageHandler.sendConfirmation(message, selectedUser.getEmail());
                clearView();
            } catch (Exception e) {
                System.out.println("Email doesn't exist");
            }
        }
    }


    /**
     * Disable certain buttons depending on the state of transaction
     *
     * @param transactions
     */
    private void disableButtons(Transactions transactions) {
        if(transactions.getPaymentMethod()==0 && transactions.getCompleted()==1){
            validateButton.setDisable(true);
            rejectButton.setDisable(true);
            refundButton.setDisable(false);
        } else if(transactions.getPaymentMethod()==0 &&(transactions.getCompleted()==0 || transactions.getCompleted() ==3 || transactions.getCompleted()==2)){
            validateButton.setDisable(true);
            rejectButton.setDisable(true);
            refundButton.setDisable(true);
        } else {
            if (transactions.getCompleted() == 0) {
                validateButton.setDisable(false);
                rejectButton.setDisable(false);
                refundButton.setDisable(true);
            } else if (transactions.getCompleted() == 1) {
                validateButton.setDisable(true);
                rejectButton.setDisable(true);
                refundButton.setDisable(false);
            } else if (transactions.getCompleted() == 2) {
                validateButton.setDisable(false);
                rejectButton.setDisable(true);
                refundButton.setDisable(true);
            } else if (transactions.getCompleted() == 3) {
                validateButton.setDisable(true);
                rejectButton.setDisable(true);
                refundButton.setDisable(true);
            }
        }
    }


    /**
     * This method switches scene to home
     *
     * @throws IOException view not found exception
     * @param event method trigger {@link Event}
     */
    @FXML
    private void goHome(Event event) throws IOException {
        try{
            Convenience.openHome();
        }catch(Exception ex){
            Convenience.showAlert(CustomAlertType.WARNING, "Oops, something went wrong. Please, try again later.");
        }
    }

    /**
     * This method starts a DB transaction
     *
     * @param status transaction status {@link int}
     * @throws Exception Connection exception {@link Exception}
     */
    private void startTransaction(int status){
        try {
            selectedTransaction.setCompleted(status);
            if(status == 3)
                selectedEvent.setAvailablePlaces(selectedEvent.getAvailablePlaces() + 1);
            entityManager.getTransaction().begin();
            entityManager.merge(selectedEvent);
            entityManager.merge(selectedTransaction);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            handleConnection();
        }
    }

    /**
     * Method which clears the views after a transaction has been processed
     */
    private void clearView(){
        selectedTransaction= null;
        selectedUser = null;
        selectedEvent = null;

        validateButton.setDisable(true);
        rejectButton.setDisable(true);
        refundButton.setDisable(true);

        transactionLabel.setText("");
        eventidLabel.setText("");
        eventnameLabel.setText("");
        eventdateLabel.setText("");
        priceLabel.setText("");
        studentnameLabel.setText("");
        studentemailLabel.setText("");
        statusLabel.setText("");
        paymethodLabel.setText("");
        companyLabel.setText("");
    }

    /**
     * This method handles the loss of internet connection
     * delegating it to NoInternet controller
     */
    public synchronized void handleConnection(){
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
