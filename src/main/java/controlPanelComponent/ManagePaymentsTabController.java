package controlPanelComponent;

import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import models.Transactions;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the managePaymentstTab view
 *
 * @author Gheorghe Mironica
 */
public class ManagePaymentsTabController implements Initializable {

    @FXML
    private Label transactionLabel, eventidLabel, eventnameLabel, eventdateLabel, priceLabel, studentnameLabel, studentemailLabel, statusLabel, homeLabel;
    @FXML
    private Button validateButton, rejectButton, refundButton;
    @FXML
    private RadioButton radioAll, radioOpen, radioProcessed;
    @FXML
    private ListView<Transactions> transactionsList;

    /**
     * This method initializes the views
     *
     * @param url input parameter
     * @param resourceBundle input parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



    /**
     * This method switches scene to home
     *
     * @throws IOException view not found exception
     * @param event method trigger {@link Event}
     */
    @FXML
    private void goHome(Event event) throws IOException {
        Convenience.switchScene(event, getClass().getResource("/FXML/mainUI.fxml"));
    }
}
