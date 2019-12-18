package listComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import handlers.CacheSingleton;
import handlers.Convenience;
import handlers.LRUCache;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.Thread;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * This class serves as a controller for the custom ListCell of the ListView
 *
 * @author Gheorghe Mironica
 */
public class CustomListViewCell extends JFXListCell<Events> {

    @FXML
    private ImageView cellLogo;

    @FXML
    private Label descriptionLabel, locationLabel, priceLabel, availableLabel;

    @FXML
    private JFXButton wishlistButton;

    @FXML
    private HBox boxLayout;
    private FXMLLoader loader;
    private Image image;
    private String imageURL;
    private int id;
    private EntityManager entityManager;
    private String city;
    private Account account;
    private Events currentEvent;

    @Override
    protected synchronized void updateItem(Events event, boolean empty) {
        super.updateItem(event, empty);

        if (empty || event == null) {
            setText(null);
            setGraphic(null);
            account = CurrentAccountSingleton.getInstance().getAccount();
            entityManager = account.getConnection();

        } else {
            id = event.getId();
            currentEvent = event;

            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/FXML/listcell.fxml"));
                loader.setController(this);
            }
            try {
                loader.load();
            } catch (Exception e) {
//                Convenience.showAlert(Alert.AlertType.ERROR,
//                        "Error", "Something went wrong", "Please, try again later");
            }


//            try {
                 CacheSingleton cache = CacheSingleton.getInstance();
//
//                if (cache.containsEventInWishlist(id) && cache.isEventInWishlist(id)) {
//                    wishlistButton.setText("Wishlist --");
//                } else if (cache.containsEventInWishlist(id) && !cache.isEventInWishlist(id)) {
//                    wishlistButton.setText("Wishlist ++");
//                } else {
//                    boolean inWishlist = account.getEvents().contains(event);
//                    if (inWishlist) {
//                        wishlistButton.setText("Wishlist --");
//                    } else {
//                        wishlistButton.setText("Wishlist ++");
//                    }
//                    cache.putEventInWishlist(id, inWishlist);
//                }
//
//                if (account instanceof Admin) {
//                    wishlistButton.setDisable(true);
//                }

             //   if (!(account instanceof Admin)) {
            //       if (cache.containsEventBooked(id) && cache.isEventBooked(id)) {
            //            wishlistButton.setDisable(true);
            //        } else if (cache.containsEventBooked((id)) && !cache.isEventBooked(id)) {
            //            wishlistButton.setDisable(false);
            //        } else {
            //           boolean isBooked = isBooked();
            //            wishlistButton.setDisable(isBooked);
            //            cache.putEventBooked(id, isBooked);
             //       }
             //   }

            try{
                checkIfInWishlist();
                if(isBooked()|| account instanceof Admin) {
                    wishlistButton.setDisable(true);
                } else{
                    wishlistButton.setDisable(false);
                }

                if (cache.containsImage(id)) {
                    image = cache.getImage(id);
                } else {
                    imageURL = currentEvent.getPicture().getLogo();
                    image = new Image(imageURL);
                    cache.putImage(id, image);
                }

                city = entityManager.find(Location.class, id).getCity();
                cellLogo.setImage(image);
                cellLogo.setFitHeight(120);
                cellLogo.setFitWidth(120);
            } catch (Exception e) {
              //  Convenience.showAlert(Alert.AlertType.ERROR,
              //          "Error", "Something went wrong", "Please, try again later");
                image = new Image("/IMG/quest.png");
                cellLogo.setImage(image);
            }

            descriptionLabel.setText(event.getShortDescription());
            locationLabel.setText(city);
            availableLabel.setText(String.valueOf(event.getAvailablePlaces()));
            setText(null);
            setGraphic(boxLayout);
            if (event.getPrice() == 0) {
                priceLabel.setText("FREE");
            } else priceLabel.setText(String.valueOf(event.getPrice()));
        }
    }

    @FXML
    public void wishlistAction(Event e) {
        try {
            if (wishlistButton.getText().equals("Wishlist ++")) {
                wishlistButton.setText("Wishlist --");
                List<Events> l1 = account.getEvents();
                l1.add(currentEvent);
                account.setEvents(l1);
            } else {
                wishlistButton.setText("Wishlist ++");
                List<Events> l1 = account.getEvents();
                l1.remove(currentEvent);
                account.setEvents(l1);
            }
            Thread merge = new Thread(() -> {
                CountDownLatch latch = new CountDownLatch(1);
                try {
                    latch.await();
                    entityManager.getTransaction().begin();
                    entityManager.merge(account);
                    entityManager.getTransaction().commit();
                } catch (InterruptedException ex) {
                }
            });

            merge.start();

        } catch (Exception ex) {
            Convenience.showAlert(Alert.AlertType.INFORMATION, "Unavailable Event", "This event is currently unavailable or deleted ", "");
            return;
        }
    }

    protected void checkIfInWishlist(){
        List<Events> l1 = account.getEvents();
//        return account.checkEventPresence(entityManager, currentEvent.getId());
        boolean ok = account.checkEventPresence(entityManager, currentEvent.getId());
        if (ok){
            wishlistButton.setText("Wishlist --");
        } else{
            wishlistButton.setText("Wishlist ++");
        }
    }

    private boolean isBooked() {
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Transactions> tq1 = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
        tq1.setParameter("id", currentEvent.getId());
        tq1.setParameter("userId", account.getId());
        int size = tq1.getResultList().size();
        return size > 0;
    }
}