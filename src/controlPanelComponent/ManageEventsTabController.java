package controlPanelComponent;

import handlers.Convenience;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import listComponent.EventListSingleton;
import models.Events;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class which serves as a controller for the events management view
 *
 * @author Gheorghe Mironica
 */
public class ManageEventsTabController implements Initializable {

    private Events selectedEvent;

    @FXML
    private TextArea shortField, longField;
    @FXML
    private TextField companyField, priceField, cityField, latitudeField, longitudeField;
    @FXML
    private DatePicker dateField;
    private ObservableList<Events> eventsObservableList;
    @FXML
    private ListView<Events> mEventsList;
    private LocalDate localDate;
    @FXML
    private ComboBox placesCombo;
    private List<Integer> comboList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            EventListSingleton events = EventListSingleton.getInstance();
            eventsObservableList = events.getEventsObservableList();
            mEventsList.setItems(eventsObservableList);
            mEventsList.setCellFactory((editListView) -> new EditListViewCell());
            comboList = new ArrayList<>();
            for(int i=1; i<100; i++){
                comboList.add(i);
            }
            longField.setWrapText(true);
            placesCombo.setItems(FXCollections.observableList(comboList));
            placesCombo.getSelectionModel().selectFirst();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method handles the click on the list cell
     *
     * @param event the mouse click event which triggered this method
     */
    @FXML
    private void cellClicked(Event event){
        try {
            selectedEvent = mEventsList.getSelectionModel().getSelectedItem();
            System.out.println(selectedEvent.getId());
                //fetch data to fill the form
            fillFormFromSelectedEvent();
        }catch(Exception e){
            // list is updating
        }
    }


    /**
     * Switches scene to the main one
     *
     * @param event the mouse click event which triggered this method
     * @throws IOException
     */
    @FXML
    private void goHome(Event event) throws IOException {
        Convenience.switchScene(event, getClass().getResource("/mainUI/mainUi.fxml"));
    }

    /**
     * Method which fetches the date from the selected event and fills the form
     */
    private void fillFormFromSelectedEvent() {
        companyField.setText(selectedEvent.getCompany());
        priceField.setText(String.valueOf(selectedEvent.getPrice()));
        localDate = selectedEvent.getDate().toLocalDate();
        dateField.setValue(localDate);
        cityField.setText(selectedEvent.getLocation().getCity());
        latitudeField.setText(String.valueOf(selectedEvent.getLocation().getLatitude()));
        longitudeField.setText((String.valueOf(selectedEvent.getLocation().getLongitude())));
        shortField.setText(selectedEvent.getShortDescription());
        longField.setText(selectedEvent.getLongDescription());
        placesCombo.getSelectionModel().select(selectedEvent.getTotalPlaces()-1);
    }

    /**
     * Method which clears the input form
     *
     * @param event
     */
    @FXML
    private void clearForm(Event event){
        companyField.clear();
        priceField.clear();
        cityField.clear();
        latitudeField.clear();
        longitudeField.clear();
        shortField.clear();
        longField.clear();
        placesCombo.getSelectionModel().selectFirst();
    }

    /**
     * Method which persists the changes to the specific event
     *
     * @param event
     */
    @FXML
    private void saveChanges(Event event){
        String company = companyField.getText();
        String price = priceField.getText();
        String city = cityField.getText();
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();
        String shortF = shortField.getText();
        String longF = longField.getText();

        if(!validateInput(company,price,city,latitude,longitude,shortF,longF))
            return;

        // if input is valid, change the state of the event and persist it
        System.out.println("Changes Saved");
    }

    /**
     * Method which validates the Form Inputs
     *
     * @param companyName the company name {@link Integer}
     * @param priceValue the price {@link Double}
     * @param cityName the city name {@link String}
     * @param latitude the latitude {@link Double}
     * @param longitude the longitude {@link Double}
     * @param shortField the short description {@link String}
     * @param longField the long description {@link String}
     * @return boolean answer {@link Boolean}
     */
    private boolean validateInput(String companyName, String priceValue, String cityName, String latitude, String longitude, String shortField, String longField){
        boolean ok = true;
        boolean validCompany = (!(companyName.isEmpty())&&(companyName.matches("^[a-zA-Z]*$")));
        boolean validCity = (!(cityName.isEmpty())&&(cityName.matches("^[a-zA-Z]*$")));
        boolean validShort = (!(shortField.isEmpty())&&(shortField.length()>10));
        boolean validLong = (!(longField.isEmpty())&&(longField.length()>20));

        // validate longitude,latitude,price

        // check which are wrong
        if(!validCompany){
            companyField.setText("Invalid Name");
            companyField.setStyle("-fx-text-inner-color: red;");
             ok = false;
        }

        return ok;
    }

    /**
     * Method which creates the event
     *
     * @param event the event which triggers this method  {@link javafx.scene.input.MouseEvent}
     */
    @FXML
    private void createEvent(Event event){
        String company = companyField.getText();
        String price = priceField.getText();
        String city = cityField.getText();
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();
        String shortF = shortField.getText();
        String longF = longField.getText();

        if(!validateInput(company,price,city,latitude,longitude,shortF,longF))
            return;

    }


    /**
     * Reset the text color of the company field to black when clicked, clear it
     *
     * @param event event which triggered the method {@link javafx.scene.input.MouseEvent}
     */
    @FXML
    private void companyFieldClicked(Event event){
        companyField.clear();
        companyField.setStyle("-fx-text-inner-color: black;");
    }

    /**
     * Reset the text color of the price field to black when clicked, clear it
     *
     * @param event event which triggered the method {@link javafx.scene.input.MouseEvent}
     */
    @FXML
    private void priceFieldClicked(Event event){
        priceField.clear();
        priceField.setStyle("-fx-text-inner-color: black;");
    }

}
