package wishlistComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import listComponent.EventListSingleton;
import mainUI.MainPane;
import models.Account;
import models.Events;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * A class that controls the wishlist view
 * @author Hidayat Rzayev
 */
public class WishlistController implements Initializable {

    @FXML
    private JFXListView<Events> wishListView;

    @FXML
    private JFXButton bookAllButton;

    @FXML
    private JFXButton bookSelectedButton;

    @FXML
    private JFXButton removeAllButton;

    private Account currentAccount;
    private ObservableList<Events> observableWishList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        List<Events> eventsInWishList = currentAccount.getEvents();

        observableWishList = FXCollections.observableArrayList(eventsInWishList);

        // allow multiple events to be selected
        wishListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        wishListView.setItems(observableWishList);

        wishListView.setCellFactory(wishlistCell -> new WishlistCellController());

        wishListView.setPlaceholder(new Label("Your wishlist is empty."));

        initButtonsDisableProperty();
    }

    /**
     * Binds the disable property of the 3 buttons with the size of the wishlist.
     * If the wishlist is empty, the "Book all" and "Remove all" buttons will be disabled.
     * If no item from the wishlist is selected, the "Book selected" button will be disabled.
     */
    private void initButtonsDisableProperty() {
        BooleanBinding wishlistSizeProperty = Bindings.size(observableWishList).isEqualTo(0);
        BooleanBinding selectedItemsSizeProperty = Bindings.size(wishListView.getSelectionModel().getSelectedItems()).isEqualTo(0);
        bookAllButton.disableProperty().bind(wishlistSizeProperty);
        bookSelectedButton.disableProperty().bind(selectedItemsSizeProperty);
        removeAllButton.disableProperty().bind(wishlistSizeProperty);
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
        jumpToBooking();
    }

    /**
     * Removes all the events from the wishlist.
     *
     * @param actionEvent - the event that triggered the method
     */
    @FXML
    private void handleRemoveAllClicked(MouseEvent actionEvent) {
        if (userConfirmsRemoveAll()) {
            List<Events> backup = new ArrayList<>(currentAccount.getEvents());
            EntityManager entityManager = currentAccount.getConnection();
            currentAccount.getEvents().clear();

            try {
                entityManager.getTransaction().begin();
                entityManager.merge(currentAccount);
                entityManager.getTransaction().commit();
            } catch (RuntimeException re) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                currentAccount.setEvents(backup);
                if (!HandleNet.hasNetConnection()) {
                    showNoInternet();
                } else {
                    Convenience.showAlert(CustomAlertType.ERROR,
                            "Oops, something went wrong. Please, try again later.");
                }
                return;
            }

            EventListSingleton.getInstance().refreshList();
            observableWishList.clear();
        }
    }

    /**
     * Shows the alert to the user asking if he/she indeed wants to remove all the events from the wishlist.
     *
     * @return {@code true} if the user clicks "Yes", {@code false} otherwise.
     */
    private boolean userConfirmsRemoveAll() {
        Optional<ButtonType> response = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Are you sure you want to remove all the events from your wishlist?",
                ButtonType.YES, ButtonType.CANCEL);
        return response.isPresent() && response.get() == ButtonType.YES;
    }

    /**
     * Books all the events in the wishlist.
     *
     * @param mouseEvent - the event that triggered the method
     */
    @FXML
    private void handleBookAllClicked(MouseEvent mouseEvent) {
        currentAccount.setBookedEvents(observableWishList);
        jumpToBooking();
    }

    /**
     * Jumps to the booking page to proceed with booking.
     */
    private void jumpToBooking() {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/booking.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }

    /**
     * Shows "No Internet Connection" popup to the user.
     */
    private void showNoInternet() {
        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/noInternet.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }
}
