package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Events;
import models.Transactions;

import javax.persistence.EntityManager;

public class TransactionsListViewCell extends ListCell<Transactions> {

    @FXML
    private Label transactionID, studentEmail, status, timeLabel;
    @FXML
    private HBox cellLayout;
    private FXMLLoader loader;

    @Override
    protected synchronized void updateItem(Transactions item, boolean empty) {
        super.updateItem(item, empty);


        if(empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            if(loader == null){
                loader = new FXMLLoader(getClass().getResource("/FXML/transactionsListViewCell.fxml"));
                loader.setController(this);
            }
            try{
                loader.load();
            } catch(Exception e){
                //
            }

            try {
                transactionID.setText(String.valueOf(item.getId()));
                studentEmail.setText(item.getUser().getEmail());
                TransactionStatus statusID = TransactionStatus.valueOf(item.getCompleted());
                status.setText(String.valueOf(statusID));
                timeLabel.setText(String.valueOf(item.getDate()));
            } catch(Exception e){
                e.printStackTrace();
            }
            // set views
            setText(null);
            setGraphic(cellLayout);
        }


    }
}
