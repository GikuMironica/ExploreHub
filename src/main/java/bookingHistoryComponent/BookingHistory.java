package bookingHistoryComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXListView;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mainUI.MainPane;
import models.Account;
import models.Transactions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@SuppressWarnings("JpaQueryApiInspection")
/**
 * This is a controller class responsible for visualizing & managing the user's
 * booked events by the user.
 *
 * @author Gheorghe Mironica
 */
public class BookingHistory implements Initializable {

    @FXML
    private JFXListView<Transactions> bookingHistoryList;

    private ObservableList<Transactions> observablehistoryList;

    private Account account;

    private EntityManager entityManager;

    private Transactions selectedTransaction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Transactions> transcations = null;

        account = CurrentAccountSingleton.getInstance().getAccount();
        entityManager = account.getConnection();

        TypedQuery<Transactions> tq = entityManager.createNamedQuery("Account.findAllBookedEvents", Transactions.class);
        tq.setParameter("id", account.getId());

        try {
            transcations = tq.getResultList();
            entityManager.refresh(account);
        }catch(Exception e){
            try {
                if(!HandleNet.hasNetConnection()) {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                }else {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            }catch(Exception exc) { /**/ }
        }

        if(!(transcations == null)){
            observablehistoryList = FXCollections.observableArrayList(transcations);
            bookingHistoryList.setItems(observablehistoryList);
        }
        bookingHistoryList.setCellFactory(historyCell -> new BookingHistoryCell());
        bookingHistoryList.setPlaceholder(new Label("You have no booked events."));

    }

}
