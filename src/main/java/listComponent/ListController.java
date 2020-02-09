package listComponent;


import authentification.loginProcess.CurrentAccountSingleton;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import mainUI.MainPane;
import models.Account;
import models.Events;

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
    @FXML
    private VBox listLayout;
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
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception ex) { /**/ }
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
        EventListSingleton.getInstance().setEventsListView(EventList);

    }

    /**
     * Method which starts a new scene and displays information about the selected event
     * @param event
     */
    @FXML
    private void cellClicked(Event event){
        selectedEvent = EventList.getSelectionModel().getSelectedItem();
        try {
            if(HandleNet.hasNetConnection()) {
                EventWindowController eventWindowController = Convenience.popupDialog(
                        MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/eventwindow.fxml"));
                eventWindowController.initModel(selectedEvent);
            } else {
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                }catch(Exception e) { /**/ }
            }
        } catch (Exception e) {
        }

    }

}
