package wishlistComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXListCell;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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

    private int eventId;

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
                    Convenience.showAlert(Alert.AlertType.ERROR,
                            "Error", "Something went wrong", "Please, try again later");
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

        eventId = event.getId();
        String logoURL = entityManager.find(Pictures.class, eventId).getLogo();
        String city = entityManager.find(Location.class, eventId).getCity();

        Image eventLogo = new Image(logoURL);
        logoImageView.setImage(eventLogo);

        titleLabel.setText(event.getShortDescription());
        locationLabel.setText(city);
        priceLabel.setText(event.getPrice().toString() + "€");

        setGraphic(wishlistCellAnchorPane);
    }

    /**
     * Jumps to the booking page to book the selected event.
     *
     * @param mouseEvent - the event that triggered the method
     */
    @FXML
    private void handleBookClicked(MouseEvent mouseEvent) {
        EntityManager entityManager = currentAccount.getConnection();
        Events bookedEvent = entityManager.find(Events.class, eventId);

        List<Events> bookedEvents = new ArrayList<>();
        bookedEvents.add(bookedEvent);
        currentAccount.setBookedEvents(bookedEvents);

        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/booking.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
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
        Events selectedEvent = entityManager.find(Events.class, eventId);

        currentAccount.getEvents().remove(selectedEvent);
        entityManager.getTransaction().begin();
        entityManager.merge(currentAccount);
        entityManager.getTransaction().commit();

        this.getListView().getItems().remove(selectedEvent);
    }
}
