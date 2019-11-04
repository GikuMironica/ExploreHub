package listComponent;


import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import models.Events;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    @FXML
    private ListView<Events> EventList;
    private ObservableList<Events> eventsObservableList;
    private UserConnectionSingleton con;
    private EntityManager entityManager;
    private List<Events> tempList;

    public ListController(){
        try{
            con = UserConnectionSingleton.getInstance();
            entityManager = con.getManager();
            TypedQuery<Events> tq1 = entityManager.createNamedQuery("Events.findAllEvents", Events.class);
            tempList = tq1.getResultList();
            System.out.println(tempList.size());
            eventsObservableList = FXCollections.observableArrayList();
            eventsObservableList.addAll(tempList);
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EventList.setItems(eventsObservableList);
        EventList.setCellFactory(customListViewCell -> new CustomListViewCell());
    }
}
