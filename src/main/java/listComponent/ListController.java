package listComponent;


import authentification.CurrentAccountSingleton;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import models.Account;
import models.Events;
import models.User;

import javax.persistence.EntityManager;
import java.net.URL;
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
    private EntityManager entityManager;
    private Account account;

    /**
     * Custom Constructor
     */
    public ListController(){
        try{
            EventListSingleton events = EventListSingleton.getInstance();
            eventsObservableList = events.getEventsObservableList();
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            e.printStackTrace();
        }
    }

    /**
     * Method which initializes the vies
     * @param location location {@link URL}
     * @param resources resource {@link ResourceBundle}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventList.setItems(eventsObservableList);
        EventList.setCellFactory(customListViewCell -> new CustomListViewCell());
        account = CurrentAccountSingleton.getInstance().getAccount();
        entityManager = account.getConnection();
    }

    /**
     * Method which starts a new scene and displays information about the selected event
     * @param event
     */
    @FXML
    private void cellClicked(Event event){
        selectedEvent = EventList.getSelectionModel().getSelectedItem();


        try {
            entityManager.refresh(selectedEvent);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/eventwindow.fxml"));
            ScrollPane root = loader.load();
            Scene scene = new Scene(root);
            EventWindowController eventWindowController = loader.getController();
            eventWindowController.initModel(selectedEvent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
