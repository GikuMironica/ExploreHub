package listComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.*;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This is the controller class for the selected event from the ListView
 * @author Gheorghe Mironica
 */
public class EventWindowController{

    private Events currentEvent;
    @FXML
    private TextFlow longDescription;
    @FXML
    private Button back, book, wishList;
    @FXML
    private Label dateData, considering, placesData, locationData, title, priceData;
    @FXML
    private ImageView imageView;
    private double price;
    private int available, total, consider;
    private Image image;
    private UserConnectionSingleton con;
    private EntityManager entityManager;
    private Account account;

    public void initModel(Events event){
        this.currentEvent = event;
        account = CurrentAccountSingleton.getInstance().getAccount();
        if(checkUser())
            checkIfInWishlist();

        // get connection
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();

        // get picture associated with this event
        int id = currentEvent.getId();
        String imageURL = entityManager.find(Pictures.class, id).getPicture();
        image = new Image(imageURL);
        imageView.setImage(image);
        imageView.setFitHeight(333.0);
        imageView.setFitWidth(464.0);

        longDescription.getChildren().add(new Text(currentEvent.getLongDescription()+"\n"+
                "We need to add few more lines\n"+
                "Here here here here here here \n"+
                "And here here here here"));
        title.setText(currentEvent.getShortDescription());

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
        consider = currentEvent.getCheckedIN();
        sanityCheck();
        placesData.setText(available+"/"+total);
        considering.setText(consider+" Students added it to Wishlist");

    }

    private void sanityCheck() {
        if (available>total){
            available = total;
        }
        if(consider<0){
            consider =0;
            currentEvent.setCheckedIN(0);
        }
    }

    private boolean checkUser() {
        if(account instanceof Admin){
            book.setVisible(false);
            wishList.setVisible(false);
            return false;
        }else{
            book.setVisible(true);
            wishList.setVisible(true);
            return true;
        }
    }
    private void checkIfInWishlist(){
        List<Events> l1 = ((User)(account)).getEvents();
        for( Events temp : l1){
            if (temp.getId() == currentEvent.getId()){
                wishList.setText("Remove From Wishlist");
            }
        }
    }

    @FXML
    private void addremoveWishList(Event event){
        if(wishList.getText().matches("Add to Wishlist")){
            wishList.setText("Remove From Wishlist");
            List<Events> l1 = ((User)(account)).getEvents();
            l1.add(currentEvent);
            currentEvent.setCheckedIN(consider+1);
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();

        }else{
            wishList.setText("Add to Wishlist");
            List<Events> l1 = ((User)(account)).getEvents();
            l1.remove(currentEvent);
            ((User)(account)).setEvents(l1);
            currentEvent.setCheckedIN(consider-1);
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
        }
    }

    private boolean available() {
        if(currentEvent.getAvailablePlaces()==0){
            return false;
        }else
            return true;
    }

    @FXML
    private void goBack(Event event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/mainUI/mainUI.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            ex.printStackTrace();
            return;
        }
    }
}
