package listComponent;


import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Events;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is the controller class for the events ListView
 * @author Gheorghe Mironica
 */
public class ListController implements Initializable {

    @FXML
    private ListView<Events> EventList;
    private ObservableList<Events> eventsObservableList;
    private Events selectedEvent;

    public ListController(){
        try{
            EventListSingleton events = EventListSingleton.getInstance();
            eventsObservableList = events.getEventsObservableList();
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EventList.setItems(eventsObservableList);
        EventList.setCellFactory(new Callback<ListView<Events>, ListCell<Events>>() {
            @Override
            public ListCell<Events> call(ListView<Events> customListViewCell) {
                return new CustomListViewCell();
            }
        });

    }

    /**
     * Method which starts a new scene and displays information about the selected event
     * @param event
     */
    @FXML
    private void cellClicked(Event event){
        selectedEvent = EventList.getSelectionModel().getSelectedItem();
       //Check the internet connection first
        // then pass the selected event to the new scene
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/listComponent/eventwindow.fxml"));
            ScrollPane root = loader.load();
            Scene scene = new Scene(root);
            EventWindowController eventWindowController = loader.getController();
            eventWindowController.initModel(selectedEvent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            ex.printStackTrace();
            return;
        }

    }

}
