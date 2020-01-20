package controlPanelComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.*;
import handlers.CacheSingleton;
import handlers.Convenience;
import handlers.HandleNet;
import handlers.UploadImage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainUI.MainPane;
import models.*;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.Thread;

/**
 * Class which serves as a controller for the events management view
 *
 * @author Gheorghe Mironica
 */
public class ManageEventsTabController {

    private Events selectedEvent;

    @FXML
    private JFXRadioButton freeRadio, paidRadio;
    @FXML
    private Label longCharsRemaining, shortCharsRemaining, priceLabel;
    @FXML
    private JFXButton saveButton, deleteButton, picButton, logoButton;
    @FXML
    private JFXTextArea shortField, longField;
    @FXML
    private JFXTextField companyField, priceField, cityField, latitudeField, longitudeField;
    @FXML
    private JFXDatePicker dateField;
    private ObservableList<Events> eventsObservableList;
    @FXML
    private JFXListView<Events> mEventsList;
    private LocalDate localDate;
    @FXML
    private JFXComboBox placesCombo;
    private List<Integer> comboList;
    private final String LATITUDE_PATTERN="^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$";
    private final String LONGITUDE_PATTERN="^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$";
    private final String PRICE_PATTERN="^(-?)(0|([1-9][0-9]*))((.|,)[0-9][0-9]?)?$";
    private final String CITY_PATTERN="^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    private final String ORGANISATION_PATTERN="^[A-Z]([a-zA-Z0-9]|[- @\\.#&!])*$";
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private final EntityManager entityManager = admin.getConnection();
    private final int longDescriptionUpLimit = 500;
    private final int shortDescriptionUpLimit = 50;
    private final int longDescriptionLowLimit = 20;
    private final int shortDescriptionLowLimit = 10;
    private Image logoPic;
    private Image mainPic;

    /**
     * Method which initializes the view ManageEventsTabController
     */
    public void initialize(List<Events> eventList) {
        try{
            eventsObservableList = FXCollections.observableArrayList();
            eventsObservableList.addAll(eventList);
            mEventsList.setItems(eventsObservableList);
            saveButton.setDisable(true);
            deleteButton.setDisable(true);
            mEventsList.setCellFactory((editListView) -> new EventListViewCell());
            comboList = new ArrayList<>();
            for(int i=1; i<100; i++){
                comboList.add(i);
            }
            longField.setWrapText(true);
            shortField.setWrapText(true);
            placesCombo.setItems(FXCollections.observableList(comboList));
            placesCombo.getSelectionModel().selectFirst();
            logoButton.setStyle("-fx-text-fill: red;");
            picButton.setStyle("-fx-text-fill: red;");
            shortCharsRemaining.setText(String.valueOf(shortDescriptionUpLimit-shortField.getText().length()));
            longCharsRemaining.setText(String.valueOf(longDescriptionUpLimit-longField.getText().length()));

            //restrict user to input extra characters
            longField.setTextFormatter(new TextFormatter<String>(change ->
                    change.getControlNewText().length() <= longDescriptionUpLimit ? change : null));
            shortField.setTextFormatter(new TextFormatter<String>(change ->
                    change.getControlNewText().length() <= shortDescriptionUpLimit ? change : null));

            // listener to the descriptions
            longField.textProperty().addListener((observable, oldValue, newValue) -> {
                longCharsRemaining.setText(String.valueOf(longDescriptionUpLimit - longField.getText().length()));
            });
            shortField.textProperty().addListener((observable, oldValue, newValue) -> {
                shortCharsRemaining.setText(String.valueOf(shortDescriptionUpLimit - shortField.getText().length()));
            });

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
            clearForm(event);
            selectedEvent = mEventsList.getSelectionModel().getSelectedItem();
            deleteButton.setDisable(false);
            saveButton.setDisable(false);
                //fetch data to fill the form
            fillFormFromSelectedEvent();
        }catch(Exception e){
            // list is updating
        }
    }

    /**
     * Method which persists the changes made to the specific event
     *
     * @param event trigger of the event
     */
    @FXML
    private void updateEvent(Event event){

        if(invalidForm()) {
            return;
        }

        int tt = selectedEvent.getTotalPlaces();
        int av = selectedEvent.getAvailablePlaces();
        int totalSelected = placesCombo.getSelectionModel().getSelectedIndex();
        totalSelected++;
        if(totalSelected>=tt){
            av+= (totalSelected-tt);
        } else{
            av -= (tt-totalSelected);
        }

        if(totalSelected<av){
            selectedEvent.setAvailablePlaces(totalSelected);
        }

        localDate = dateField.getValue();
        Date actualDate = Date.valueOf(localDate);

        selectedEvent.setDate(actualDate);
        selectedEvent.setCompany(companyField.getText());
        if(selectedEvent instanceof Excursion) {
            selectedEvent.setPrice(Double.valueOf(priceField.getText()));
        }
        selectedEvent.getLocation().setCity(cityField.getText());
        selectedEvent.getLocation().setLatitude(Double.valueOf(latitudeField.getText()));
        selectedEvent.getLocation().setLongitude(Double.valueOf(longitudeField.getText()));
        selectedEvent.setShortDescription(shortField.getText());
        selectedEvent.setLongDescription(longField.getText());
        selectedEvent.setTotalPlaces(totalSelected);
        selectedEvent.setAvailablePlaces(av);

        if(!arePicturesValid()) {
            return;
        }

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(selectedEvent);
            entityManager.getTransaction().commit();
        } catch(Exception e){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception exc) {
                exc.printStackTrace();
                Convenience.showAlert(CustomAlertType.ERROR, "Update attempt unsuccessfully, contact developers team.");
            }
            return;
        }

        clearForm(event);
        mainPic = null;
        logoPic = null;
        Convenience.showAlert(CustomAlertType.SUCCESS, "The event has been successfully updated!");
    }


    /**
     * Method which creates the event
     *
     * @param event the event which triggers this method  {@link Event}
     */
    @FXML
    private void createEvent(Event event){

        if(invalidForm()) {
            return;
        }

        if(logoPic==null || mainPic == null){
            Convenience.showAlert(CustomAlertType.WARNING, "Please, insert the main picture for the event as well as the company logo.");
            return;
        }

        String urlLogo = uploadIMG(mainPic);
        String urlPic = uploadIMG(logoPic);


        // if imgur threw http 400, too large img, invalid content
        if(urlLogo == null || urlPic == null){
            return;
        }
        if(urlLogo.isBlank() || urlPic.isBlank()) {
            clearPictureButton();
            Convenience.showAlert(CustomAlertType.WARNING,"One of the pictures is too wide or contains inappropriate content. Please, choose another picture.");
            return;
        }


        // Normal
        Double price = 0.0;
        int totalSelected = placesCombo.getSelectionModel().getSelectedIndex()+1;
        Date actualDate = null;
        try {
             actualDate = Date.valueOf(dateField.getValue());
        }catch(Exception e){
            Convenience.showAlert(CustomAlertType.WARNING, "Please, choose a day after tomorrow again.");
            return;
        }
        Double latitude = Double.valueOf(latitudeField.getText());
        Double longitude = Double.valueOf(longitudeField.getText());
        Events newEvent = null;


        if(paidRadio.isSelected()){
            price = Double.valueOf(priceField.getText());
            newEvent = new Excursion(actualDate, Double.valueOf(price), totalSelected, totalSelected, shortField.getText(), longField.getText());
        }else if (freeRadio.isSelected()){
            String company = companyField.getText();
            newEvent = new CompanyExcursion(actualDate, company, totalSelected, totalSelected, shortField.getText(), longField.getText());
        }

        Location newLoc = new Location(Double.valueOf(latitude), Double.valueOf(longitude), cityField.getText());
        Pictures newPic = new Pictures(urlLogo, urlPic);

        if(isPersistedOk(newEvent, newLoc, newPic)) {
            clearForm(event);
            mainPic = null;
            logoPic = null;
            Convenience.showAlert(CustomAlertType.SUCCESS, "The event has been successfully created!");
        }else{
            clearForm(event);
            mainPic = null;
            logoPic = null;
            return;
        }
    }

    /**
     * Method which deletes the selected event
     *
     * @param event the trigger of the event {@link Event}
     */
    @SuppressWarnings("JpaQueryApiInspection")
    @FXML
    private void deleteEvent(Event event){
        if(selectedEvent == null){
            Convenience.showAlert(CustomAlertType.WARNING, "Please, select an event to be deleted.");
            return;
        }
        Optional<ButtonType> response = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                "This event is going to be deleted from the database. Are you sure you want to proceed?",
                ButtonType.YES, ButtonType.CANCEL);
        if(response.isPresent() && response.get() == ButtonType.CANCEL){
            return;
        } else {
            try {
                Events ev = entityManager.find(Events.class, selectedEvent.getId());
                entityManager.getTransaction().begin();
                entityManager.remove(ev);
                entityManager.getTransaction().commit();
                eventsObservableList.remove(selectedEvent);
                Convenience.showAlert(CustomAlertType.SUCCESS, "The event has been successfully deleted!");
            }catch(Exception exc){
                exc.printStackTrace();
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                            getClass().getResource("/FXML/noInternet.fxml"));
                }catch(Exception e) { /**/ }
            }
            clearForm(event);
        }
    }

    /**
     * Method which fetches the date from the selected event and fills the form
     */
        private void fillFormFromSelectedEvent() {
        if(selectedEvent instanceof CompanyExcursion){
            freeRadioSelected();
            freeRadio.setDisable(true);
            paidRadio.setDisable(true);
            priceField.setText("Free");
        }else{
            priceField.setText(String.valueOf(selectedEvent.getPrice()));
            paidRadioSelected();
            freeRadio.setDisable(true);
            paidRadio.setDisable(true);
            priceField.setText(String.valueOf(selectedEvent.getPrice()));
        }

        companyField.setText(selectedEvent.getCompany());
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
     * Gets the result from the validation of each field
     *
     * @return Boolean Result from {@link #validateInput(String, String, String, String, String, String, String)}
     */
    protected boolean invalidForm() {
        String company = companyField.getText();
        String price = priceField.getText();
        String city = cityField.getText();
        String latitude = latitudeField.getText();
        String longitude = longitudeField.getText();
        String shortF = shortField.getText();
        String longF = longField.getText();

        return !validateInput(company,price,city,latitude,longitude,shortF,longF);
    }

    /**
     * Method which validates the Form Inputs
     *
     * @param companyName the company name {@link Integer}
     * @param priceValue the price {@link Double}
     * @param cityName the city name {@link String}
     * @param latitude the latitude {@link Double}
     * @param longitude the longitude {@link Double}
     * @param shortFieldText the short description {@link String}
     * @param longFieldText the long description {@link String}
     * @return boolean answer {@link Boolean}
     */
    protected boolean validateInput(String companyName, String priceValue, String cityName, String latitude, String longitude, String shortFieldText, String longFieldText){
        boolean ok = true;
        boolean validPrice = true;
        boolean validCompany = (!(companyName.isEmpty())&&(companyName.matches(ORGANISATION_PATTERN)));
        boolean validCity = (!(cityName.isEmpty())&&(cityName.matches(CITY_PATTERN)));
        boolean validShortDescription = (!(shortFieldText.isEmpty())&&(shortFieldText.length()>shortDescriptionLowLimit)&&(shortFieldText.length()<=shortDescriptionUpLimit));
        boolean validLongDescription = (!(longFieldText.isEmpty())&&(longFieldText.length()>longDescriptionLowLimit)&&(longFieldText.length()<=longDescriptionUpLimit));
        boolean validLatitude = (!(latitude.isEmpty()))&&(latitude.matches(LATITUDE_PATTERN));
        boolean validLongitude = (!(longitude.isEmpty()))&&(longitude.matches(LONGITUDE_PATTERN));
        if(paidRadio.isSelected()) {
            validPrice = (!(priceValue.isEmpty())) && (priceValue.matches(PRICE_PATTERN));
        }

        // check which are wrong
        if(!validCompany){
            companyField.setStyle("-fx-text-inner-color: red;");
            companyField.setText("Invalid Name");
            displayError(companyField);
            ok = false;
        }
        if(!validCity){
            cityField.setStyle("-fx-text-inner-color: red;");
            cityField.setText("Invalid Name");
            displayError(cityField);
            ok = false;
        }
        if(!validShortDescription){
            shortField.setStyle("-fx-text-inner-color: red;");
            shortField.setText("Invalid Description, Add at least 10 characters, at most 100");
            displayError(shortField);
            ok = false;
        }
        if(!validLongDescription){
            longField.setStyle("-fx-text-inner-color: red;");
            longField.setText("Invalid Description, Add at least 20 characters, at most 500");
            displayError(longField);
            ok = false;
        }
        if(!validLatitude){
            latitudeField.setText("Invalid Latitude");
            latitudeField.setStyle("-fx-text-inner-color: red;");
            displayError(latitudeField);
        }
        if(!validLongitude){
            longitudeField.setStyle("-fx-text-inner-color: red;");
            longitudeField.setText("Invalid Longitude");
            displayError(longitudeField);
        }
        if(!validPrice){
            priceField.setStyle("-fx-text-inner-color: red;");
            priceField.setText("Invalid Price");
            displayError(priceField);
        }

        LocalDate today = LocalDate.now();
        LocalDate localDate = dateField.getValue();
        if(localDate == null){
            localDate = LocalDate.now();
            dateField.setValue(localDate);
        }

        if(localDate.isEqual(today) || localDate.isBefore(today)) {
            ok = false;
            Convenience.showAlert(CustomAlertType.WARNING, "Invalid date. Please, choose a day after tomorrow.");
        }
        if((!freeRadio.isSelected())&&(!paidRadio.isSelected())){
            ok = false;
            Convenience.showAlert(CustomAlertType.WARNING, "Please, choose a type of the event you want to place.");
        }
        return ok;
    }


    /**
     * Method which clears the input form
     *
     * @param event trigger of the event
     */
    @FXML
    private void clearForm(Event event){
        longField.clear();
        shortField.clear();
        cityField.clear();
        companyField.clear();
        priceField.clear();
        longitudeField.clear();
        latitudeField.clear();
        logoButton.setText("Upload Logo");
        picButton.setText("Upload Picture");
        logoButton.setStyle("-fx-text-fill: red;");
        picButton.setStyle("-fx-text-fill: red;");
        freeRadio.setDisable(false);
        freeRadio.setSelected(false);
        paidRadio.setSelected(false);
        paidRadio.setDisable(false);
        priceField.setDisable(false);

        placesCombo.getSelectionModel().selectFirst();
    }

    /**
     * Clears particular TextField, overloaded {@link #clearForm(Event)}
     *
     * @param text {@link} TextField
     */
    private void clearForm(TextField text){
        text.setStyle("-fx-text-inner-color: black;");
        text.clear();
    }

    /**
     * Clears particular TextArea, overloaded {@link #clearForm(Event)}
     *
     * @param text {@link java.awt.TextArea}
     */
    private void clearForm(TextArea text){
        text.setStyle("-fx-text-inner-color: black;");
        text.clear();
    }

    /**
     * Clear the error message after 3 seconds from the passed in View parameter
     *
     * @param field {@link TextField}
     */
    private void displayError(TextField field) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                // don't
            }
            Platform.runLater(() -> {
                ManageEventsTabController.this.clearForm(field);
            });
        });
        thread.start();
    }

    /**
     * Clear the error message after 3 seconds from the passed in View parameter
     *
     * @param field {@link TextArea}
     */
    private void displayError(TextArea field) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                // don't
            }
            Platform.runLater(() -> {
                ManageEventsTabController.this.clearForm(field);
            });
        });
        thread.start();
    }

    /**
     * Switches scene to the main one
     *
     * @param event the mouse click event which triggered this method
     * @throws IOException file not found exception
     */
    @FXML
    private void goHome(Event event) throws IOException {
        try {
            Convenience.openHome();
        }catch(Exception e){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception ex) { /**/ }
        }
    }

    /**
     * This method loads Main Picture
     * @param event trigger of the event
     */
    @FXML
    private void uploadPic(Event event){
        // move to thread
        mainPic = uploadPicture(event, picButton);
    }

    /**
     * This method loads an image for the Logo
     * @param event trigger of the event
     */
    @FXML
    private void uploadLogo(Event event){
        // move to thread
        logoPic = uploadPicture(event, logoButton);
    }

    /**
     * Delegated method of {@link #uploadPic} {@link #uploadLogo} which loads the picture from file system
     *
     * @param event trigger of the event
     * @param imgButton {@link Image}
     * @return returns an image object {@link Image}
     */
    protected Image uploadPicture(Event event, Button imgButton){
        Image pic = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if(file!=null){
            imgButton.setText("Image Loaded");
            imgButton.setStyle("-fx-text-fill: green;");
            return new Image(file.toURI().toString());
        } else{
            return null;
        }
    }

    /**
     * Method which delegates uploading picture task to {@link UploadImage}
     *
     * @param img {@link Image}
     * @return {@link String}
     */
    private String uploadIMG(Image img) {
        UploadImage uploadImg = new UploadImage(img);
        try {
            String url = uploadImg.upload();
            return url;
        } catch (Exception e) {
            clearPictureButton();
            e.printStackTrace();
            try {
                if(!HandleNet.hasNetConnection())
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
                else
                    Convenience.showAlert(CustomAlertType.WARNING,"Currently, the server is unreachable. Please, try again later.");
            } catch (Exception exc) { /**/ }
            return null;
        }
    }

    /**
     * Method which handles the click on the radion button (free event)
     */
    @FXML
    private void freeRadioSelected(){
        priceField.setText("Free");
        priceField.setDisable(true);
        paidRadio.setSelected(false);
        freeRadio.setSelected(true);
        companyField.setDisable(false);
    }

    /**
     * Method which handles the click on the radion button (paid event)
     */
    @FXML
    private void paidRadioSelected(){
        priceField.setDisable(false);
        freeRadio.setSelected(false);
        paidRadio.setSelected(true);
        companyField.setText("Hochschule Ulm");
        companyField.setDisable(true);
    }

    /**
     * Method which persists the newly created event
     * @param newEvent {@link Events} input parameter
     * @param newLoc {@link Location} input param
     * @param newPic {@link Pictures} input param
     */
    private boolean isPersistedOk(Events newEvent, Location newLoc, Pictures newPic) {

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(newEvent);
            entityManager.getTransaction().commit();

            newLoc.setEventID(newEvent.getId());
            newPic.setEventID(newEvent.getId());
            newEvent.setLocation(newLoc);
            newEvent.setPicture(newPic);

            entityManager.getTransaction().begin();
            entityManager.merge(newEvent);
            entityManager.getTransaction().commit();
            eventsObservableList.add(newEvent);

            return true;
        } catch(Exception e){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                        getClass().getResource("/FXML/noInternet.fxml"));
                return false;
            }catch(Exception xe) { return false; }
        }

    }

    /**
     * Method which checks if pictures were uploaded
     */
    private boolean arePicturesValid() {
        boolean ok = true;
        String urlPic = null;
        String urlLogo = null;
        if(mainPic != null){
            try {
                // move to thread
                UploadImage uploadImg = new UploadImage(mainPic);
                urlPic = uploadImg.upload();
            }catch(Exception e){
                try {
                    if(!HandleNet.hasNetConnection()) {
                        Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                                getClass().getResource("/FXML/noInternet.fxml"));
                    }
                    return false;
                }catch(Exception xe) {
                    System.out.println("Picture too wide or content invalid");
                }
            }
        }
        if(logoPic != null){
            try{
                UploadImage uploadLogo = new UploadImage(logoPic);
                urlLogo = uploadLogo.upload();
                selectedEvent.getPicture().setLogo(urlLogo);
                CacheSingleton.getInstance().putImage(selectedEvent.getId(), new Image(urlLogo));
            }catch(Exception e){
                ok = false;
                try {
                    if(!HandleNet.hasNetConnection()) {
                        Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(),
                                getClass().getResource("/FXML/noInternet.fxml"));
                    }
                    return false;
                }catch(Exception xe) {
                    System.out.println("Picture too wide or content invalid");
                }
            }
        }
        try {
            if (urlPic.isBlank() || urlLogo.isBlank()) {
                clearPictureButton();
                Convenience.showAlert(CustomAlertType.WARNING, "One of the pictures is too wide or contains inappropriate content. Please, choose another picture.");
                return false;
            } else {
                selectedEvent.getPicture().setPicture(urlPic);
                selectedEvent.getPicture().setLogo(urlLogo);
            }
        }catch(NullPointerException nullException){
            clearPictureButton();
            Convenience.showAlert(CustomAlertType.WARNING, "One of the pictures is too wide or contains inappropriate content. Please, choose another picture.");
        }
        return ok;
    }

    /**
     * this method resets the text on the
     * upload picture buttons
     */
    public void clearPictureButton(){
        logoButton.setText("Upload Logo");
        picButton.setText("Upload Picture");
        logoButton.setStyle("-fx-text-fill: red;");
        picButton.setStyle("-fx-text-fill: red;");
    }
}


