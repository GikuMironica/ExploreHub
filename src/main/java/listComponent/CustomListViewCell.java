package listComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.mysql.jdbc.CommunicationsException;
import handlers.CacheSingleton;
import handlers.Convenience;
import handlers.HandleNet;
import handlers.LRUCache;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import mainUI.MainPane;
import models.*;
import org.apache.commons.lang.ObjectUtils;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.naming.CommunicationException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.Thread;
import java.net.UnknownHostException;
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
            boolean isConnecting = true;
            setText(null);
            id = event.getId();

            try {
                if (loader == null) {
                    loader = new FXMLLoader(getClass().getResource("/FXML/listcell.fxml"));
                    loader.setController(this);
                }
                loader.load();
            } catch (Exception e) { }

            CacheSingleton cache = CacheSingleton.getInstance();
            currentEvent = event;

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

                city = event.getLocation().getCity();
                cellLogo.setImage(image);
                cellLogo.setFitHeight(120);
                cellLogo.setFitWidth(120);

            descriptionLabel.setText(event.getShortDescription());
            locationLabel.setText(city);
            availableLabel.setText(String.valueOf(event.getAvailablePlaces()));
            setGraphic(boxLayout);
            if (event.getPrice() == 0) {
                priceLabel.setText("FREE");
            } else priceLabel.setText(String.valueOf(event.getPrice()));
            } catch (Exception c){
                image = new Image("/IMG/quest.png");
                cellLogo.setImage(image);
            }
        }
    }

    /**
     * This method handles the click on the add / remove wishlist button
     * @param e {@link Events} method trigger
     */
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
            wishlistButton.setDisable(true);
            persist();

        } catch (Exception ex) {
            ex.printStackTrace();
            if(!HandleNet.hasNetConnection()){
                handleConnection();
                return;
            }
            Convenience.showAlert(Alert.AlertType.INFORMATION, "Unavailable Event", "This event is currently unavailable or deleted ", "");
            return;
        }
    }

    /**
     * This method persists the result after user adds / removes event from wishlist,
     * prevents request forgery attack
     */
    private void persist(){
        Thread t1 = new Thread(()-> {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(account);
                entityManager.getTransaction().commit();
            }catch(Exception ex1){
                entityManager.merge(account);
            }

            Platform.runLater(() -> {
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(1)
                );
                visiblePause.setOnFinished(
                        (ActionEvent ev) -> {
                            wishlistButton.setDisable(false);
                        }
                );
                visiblePause.play();
            });
        });
        t1.start();
    }

    /**
     * This method checks whether the user has current event in wishlist or not.
     */
    protected void checkIfInWishlist(){
        boolean ok = account.getEvents().contains(currentEvent);
        if (ok){
            wishlistButton.setText("Wishlist --");
        } else{
            wishlistButton.setText("Wishlist ++");
        }
    }

    /**
     * This method checks whether current event is already booked by user,
     * if so, then disable add to wishlist button
     * @return
     */
    private boolean isBooked(){
        boolean nullExc = false;
        boolean exception = false;
        try {
            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Transactions> tq1 = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
            tq1.setParameter("id", currentEvent.getId());
            tq1.setParameter("userId", account.getId());
            int size = tq1.getResultList().size();
            return size > 0;
        }catch(NullPointerException ex){
            nullExc = true;
            return true;
        }catch(Exception ex) {
            exception  = true;
            return true;
        }finally {
            if((!nullExc)&&(exception)) {
                handleConnection();
            }
        }
//        for(Transactions transaction : account.getTransactions()){
//            if(transaction.getEvent().getId()==currentEvent.getId()){
//                if(transaction.getCompleted()==1 || transaction.getCompleted()==0) {
//                    System.out.println("this evennt is booked"+"  "+currentEvent.getShortDescription());
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    /**
     * This method handles the loss of internet connection
     * delegating it to NoInternet controller
     */
    public synchronized void handleConnection(){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception e) { /**/ }
        }

}
