package listComponent;

import authentification.UserConnectionSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.Events;
import models.Location;
import models.Pictures;

import javax.persistence.EntityManager;

/**
 * This class serves as a controller for the custom ListCell of the ListView
 * @author Gheorghe Mironica
 */
public class CustomListViewCell extends ListCell<Events> {

    @FXML
    private ImageView cellLogo;

    @FXML
    private Label descriptionLabel, locationLabel;

    @FXML
    private HBox boxLayout;
    private FXMLLoader loader;
    private Image image;
    private String imageURL;
    private int id;
    private UserConnectionSingleton con;
    private EntityManager entityManager;
    private String city;

    @Override
    protected void updateItem(Events event, boolean empty) {
        super.updateItem(event, empty);

        if(empty || event == null){
            setText(null);
            setGraphic(null);
        } else {
            if(loader == null){
                loader = new FXMLLoader(getClass().getResource("/listComponent/listcell.fxml"));
                loader.setController(this);
            }

            try{
                loader.load();
                con = UserConnectionSingleton.getInstance();
                entityManager = con.getManager();
            } catch(Exception e){
                // Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
                // alert.showAndWait();
                // e.printStackTrace();
            }

            id = event.getId();
            imageURL = entityManager.find(Pictures.class, id).getLogo();
            city = entityManager.find(Location.class, id).getCity();
            image = new Image(imageURL);
            cellLogo.setImage(image);

            descriptionLabel.setText(event.getShortDescription());
            locationLabel.setText(city);

            setText(null);
            setGraphic(boxLayout);
        }

    }
}
