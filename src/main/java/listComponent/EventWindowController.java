package listComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import handlers.Convenience;
import javafx.application.Platform;
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
import models.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;

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
        String imageURL = entityManager.find(Pictures.class, id).getPicture();
        image = new Image(imageURL);
        imageView.setImage(image);
        imageView.setFitHeight(333.0);
        imageView.setFitWidth(464.0);

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
        List<Events> l1 = (account).getEvents();
        boolean ok = (account).checkEventPresence(entityManager, currentEvent.getId());
           if (ok){
               wishList.setText("Remove From Wishlist");
           } else{
               wishList.setText("Add to Wishlist");
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
                List<Events> l1 = ((User) (account)).getEvents();
                l1.add(currentEvent);
                (account).setEvents(l1);
                entityManager.getTransaction().begin();
                entityManager.merge(account);
                entityManager.getTransaction().commit();
                executeOnThread();
            } else {
                wishList.setText("Add to Wishlist");
                List<Events> l1 = ((User) (account)).getEvents();
                l1.remove(currentEvent);
               (account).setEvents(l1);
                entityManager.getTransaction().begin();
                entityManager.merge(account);
                entityManager.getTransaction().commit();
                executeOnThread();
            }

        }catch(Exception e){
            e.printStackTrace();
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
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/mainUI.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            return;
        }
    }

    protected void executeOnThread(){
        Thread t1 = new Thread(() -> {
            consider = currentEvent.getCheckedIN(entityManager, currentEvent.getId());
            Platform.runLater(() -> considering.setText(consider+" Students added it to Wishlist"));
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
            Convenience.switchScene(event, getClass().getResource(("/FXML/booking.fxml")));
        }catch(IOException e){e.printStackTrace();}
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
        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Transactions> tq1 = entityManager.createNamedQuery("Transactions.findAllOngoing&Accepted", Transactions.class);
        tq1.setParameter("id", currentEvent.getId());
        tq1.setParameter("userId", account.getId());
        int size = tq1.getResultList().size();

        return size>0;
    }
}
