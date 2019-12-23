package listComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import mainUI.MainPane;
import models.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.lang.Thread;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static handlers.HandleNet.hasNetConnection;

/**
 * This is the controller class for the selected event from the ListView
 *
 * @author Gheorghe Mironica
 */
public class EventWindowController{

    private Events currentEvent;
    @FXML
    private TextFlow longDescription;
    @FXML
    private JFXButton book, wishList;
    @FXML
    private Label dateData, considering, placesData, locationData, title, priceData;
    @FXML
    private ImageView imageView;
    private Double price;
    private int available, total, consider;
    private Image image;
    private EntityManager entityManager;
    private Account account;

    /**
     * View initializer
     *
     * @param event controller trigger {@link Event}
     */
    public void initModel(Events event){
        this.currentEvent = event;
        account = CurrentAccountSingleton.getInstance().getAccount();

        // get connection
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();

        // get picture associated with this event
        int id = currentEvent.getId();

        setPicture(event);

        longDescription.getChildren().add(new Text(currentEvent.getLongDescription()));
        title.setText(currentEvent.getShortDescription());
        consider = currentEvent.getCheckedIN(entityManager, currentEvent.getId());

        locationData.setText(currentEvent.getLocation().getCity());
        dateData.setText(currentEvent.getDate().toString());

        price = currentEvent.getPrice();
        if(price<0.1){
            priceData.setText("FREE");
        } else{
            priceData.setText(String.valueOf(price)+"€");
        }

        available = currentEvent.getAvailablePlaces();
        total = currentEvent.getTotalPlaces();

        if(checkUser()) {
            hasItInWishlist();
            isEventBooked();
            isAvailable();
        }
        sanityCheck();

        placesData.setText(available+"/"+total);
        considering.setText(consider+" Students added it to Wishlist");

    }

    /**
     * Set event main picture
     * @param event {@link Events} current event
     */
    private void setPicture(Events event){
        try {
            String imageURL = event.getPicture().getPicture();
            image = new Image(imageURL);
        }catch(Exception e){
            image = new Image("/IMG/quest.png");
        }
        imageView.setImage(image);
        imageView.setFitHeight(333.0);
        imageView.setFitWidth(464.0);
    }

    /**
     * Method which checks if current event is in users wishlist
     */
    private void hasItInWishlist() {
        checkIfInWishlist();
        if(isBooked()){
            wishList.setDisable(true);
        }
    }

    /**
     * Check the validness of some views data
     */
    private void sanityCheck() {
        if (available>total){
            available = total;
        }
    }

    /**
     * Method which restricts admins from booking
     *
     * @return
     */
    protected boolean checkUser() {
        if(account instanceof Admin){
            book.setDisable(true);
            wishList.setDisable(true);
            return false;
        }else{
            book.setDisable(false);
            wishList.setDisable(false);
            return true;
        }
    }

    /**
     * Method which checks if user has current event in wishlist
     */
    protected void checkIfInWishlist(){
        try {
            boolean ok = (account).checkEventPresence(entityManager, currentEvent.getId());
            if (ok) {
                wishList.setText("Remove From Wishlist");
            } else {
                wishList.setText("Add to Wishlist");
            }
        }catch(Exception e){
            if(!hasNetConnection()){
                Convenience.showAlert(Alert.AlertType.WARNING,"Internet","Internet Connection Issues","Looks like you have connection problems, try later");
                return;
            }
            Convenience.showAlert(Alert.AlertType.INFORMATION, "Unavailable Event", "This event is currently unavailable or deleted ", "");
            return;
        }
    }

    /**
     * Method which adds / removes current event from users wishlist
     *
     * @param event method trigger {@link Event}
     */
    @FXML
    private void addremoveWishList(Event event){
        try {
            if (wishList.getText().matches("Add to Wishlist")) {
                wishList.setText("Remove From Wishlist");
                List<Events> l1 = ((account)).getEvents();
                l1.add(currentEvent);
                (account).setEvents(l1);
            } else {
                wishList.setText("Add to Wishlist");
                List<Events> l1 = ((account)).getEvents();
                l1.remove(currentEvent);
               (account).setEvents(l1);
            }
            wishList.setDisable(true);
            persist();

        }catch(Exception e){
            e.printStackTrace();
            wishList.setDisable(false);
            if(!HandleNet.hasNetConnection()){
                Convenience.showAlert(Alert.AlertType.WARNING,"Internet","Internet Connection Issues","Looks like you have connection problems, try later");
                return;
            }
            Convenience.showAlert(Alert.AlertType.INFORMATION, "Unavailable Event", "This event is currently unavailable or deleted ", "");
            return;
        }
    }

    /**
     * Method which restricts user from boooking if no more available places for current event
     *
     * @return
     */
    protected boolean isAvailable() {
        if(currentEvent.getAvailablePlaces()==0){
            book.setText("Booked Out");
            book.setDisable(true);
            return false;
        }else
            return true;
    }

    /**
     * Method which switches scene to main
     *
     * @param event {@link Event}
     */
    @FXML
    private void goBack(Event event){
        Convenience.closePreviousDialog();
    }

    /**
     * This method persists the result after user adds / removes event from wishlist,
     * prevents request forgery attack
     */
    protected void persist(){
        Thread t1 = new Thread(()-> {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(account);
                entityManager.getTransaction().commit();
                consider = currentEvent.getCheckedIN(entityManager, currentEvent.getId());
            }catch(Exception ex1){
                if(!HandleNet.hasNetConnection()){
                    Platform.runLater(()->{
                        Convenience.showAlert(Alert.AlertType.WARNING,"Internet","Internet Connection Issues","Looks like you have connection problems, try later");
                        wishList.setDisable(false);

                    });
                    return;
                }
                entityManager.merge(account);
            }
            Platform.runLater(() -> {
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(0.5)
                );
                visiblePause.setOnFinished(
                        (ActionEvent ev) -> {
                            wishList.setDisable(false);
                            considering.setText(consider+" Students added it to Wishlist");
                        }
                );
                visiblePause.play();
            });
        });
        t1.start();
    }

    /**
     * Method which books current event
     */
    @FXML
    private void bookButton(Event event){
        List<Events> currentEventList = new ArrayList<>();
        currentEventList.add(currentEvent);
        (account).setBookedEvents(currentEventList);

        book.setDisable(true);
        book.setText("Booked");

        try{
            Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                    getClass().getResource(("/FXML/booking.fxml")));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Method which checks if current event already booked
     */
    @SuppressWarnings("JpaQueryApiInspection")
    protected void isEventBooked(){
        if(isBooked()){
            book.setText("Booked");
            book.setDisable(true);
            return;
        }
    }
    protected boolean isBooked(){
        int size=0;
        try {
            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Transactions> tq1 = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
            tq1.setParameter("id", currentEvent.getId());
            tq1.setParameter("userId", account.getId());
            size = tq1.getResultList().size();
        }catch(Exception e){
            if(!HandleNet.hasNetConnection()){
                Convenience.showAlert(Alert.AlertType.WARNING,"Internet","Internet Connection Issues","Looks like you have connection problems, try later");
                book.setDisable(true);
            }
        }
        return size>0;
    }


}
