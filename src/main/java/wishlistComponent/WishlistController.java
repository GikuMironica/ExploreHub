package wishlistComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import handlers.Convenience;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.Account;
import models.Events;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * A class that controls the wishlist view
 * @author Hidayat Rzayev
 */
public class WishlistController implements Initializable {

    @FXML
    private JFXListView<Events> wishListView;

    @FXML
    private JFXButton bookSelectedButton;

    private Account currentAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        List<Events> eventsInWishList = currentAccount.getEvents();

        ObservableList<Events> observableWishList = FXCollections.observableArrayList(eventsInWishList);

        // allow multiple events to be selected
        wishListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        wishListView.setItems(observableWishList);

        wishListView.setCellFactory(wishlistCell -> new WishlistCellController());
    }

    /**
     * Gets the event(s) that a user has selected and opens the booking page with the corresponding event(s).
     *
     * @param mouseEvent - the event that triggered the method
     */
    @FXML
    private void handleBookSelectedClicked(MouseEvent mouseEvent) {
        List<Events> bookedEvents = wishListView.getSelectionModel().getSelectedItems();
        currentAccount.setBookedEvents(bookedEvents);

        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/booking.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Whenever a user clicks on an entry in the list view, the "Book selected" button will be
     * enabled or disabled accordingly.
     *
     * @param mouseEvent - the event that triggered the method
     */
    @FXML
    private void handleWishlistClicked(MouseEvent mouseEvent) {
        boolean nothingSelected = wishListView.getSelectionModel().getSelectedItems().isEmpty();
        bookSelectedButton.setDisable(nothingSelected);
    }
}
