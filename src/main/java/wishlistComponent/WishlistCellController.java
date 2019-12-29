package wishlistComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXListCell;
import handlers.CacheSingleton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import listComponent.EventListSingleton;
import mainUI.MainPane;
import models.*;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents one entry in the wishlist
 * @author Hidayat Rzayev
 */
public class WishlistCellController extends JFXListCell<Events> {

    @FXML
    private AnchorPane wishlistCellAnchorPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Label priceLabel;

    private FXMLLoader loader;
    private Account currentAccount;

    private Events event;

    @Override
    protected void updateItem(Events event, boolean empty) {
        super.updateItem(event, empty);

        currentAccount = CurrentAccountSingleton.getInstance().getAccount();

        if (empty || event == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/FXML/wishlist_cell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException ioe) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
                }
            }

            addContent(event);
        }
    }

    /**
     * Fills up the wihslist entry with the corresponding information of a particular event.
     * Namely, logo, title, location and price.
     *
     * @param event - the event whose information is to be filled
     */
    private void addContent(Events event) {
        EntityManager entityManager = currentAccount.getConnection();
        CacheSingleton cache = CacheSingleton.getInstance();

        this.event = event;
        int eventId = event.getId();

        Image eventLogo;
        if (cache.containsImage(eventId)) {
            eventLogo = cache.getImage(eventId);
        } else {
            String logoURL = event.getPicture().getLogo();
            eventLogo = new Image(logoURL);
            cache.putImage(eventId, eventLogo);
        }
        logoImageView.setImage(eventLogo);

        String city = event.getLocation().getCity();

        titleLabel.setText(event.getShortDescription());
        locationLabel.setText(city);
        priceLabel.setText(event.getPrice().toString() + "€");

        setText(null);
        setGraphic(wishlistCellAnchorPane);
    }

    /**
     * Jumps to the booking page to book the selected event.
     *
     * @param mouseEvent - the event that triggered the method
     */
    @FXML
    private void handleBookClicked(MouseEvent mouseEvent) {
        List<Events> bookedEvents = new ArrayList<>();
        bookedEvents.add(event);
        currentAccount.setBookedEvents(bookedEvents);

        try {
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource("/FXML/booking.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(CustomAlertType.ERROR, "Oops, something went wrong. Please, try again later.");
        }
    }

    /**
     * Removes the corresponding event from the wishlist and refreshes the list view.
     *
     * @param mouseEvent
     */
    @FXML
    private void handleRemoveClicked(MouseEvent mouseEvent) {
        EntityManager entityManager = currentAccount.getConnection();

        currentAccount.getEvents().remove(event);
        entityManager.getTransaction().begin();
        entityManager.merge(currentAccount);
        entityManager.getTransaction().commit();

        this.getListView().getItems().remove(event);
        EventListSingleton.getInstance().refreshList();
    }
}
