package filterComponent;

import authentification.CurrentAccountSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import models.Location;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 *Class that controls the filter view.
 * @author Aleksejs Marmiss
 *
 */
public class FilterController implements Initializable {

        @FXML
        private ComboBox<String> choiceCity;
        @FXML
        private ComboBox<String> choiceRadius;
        @FXML
        private ComboBox<String> choiceMinPers;
        @FXML
        private Slider choicePrice;
        @FXML
        private ComboBox<String> sortBy;
        @FXML
        private Label money;
        private FilterSingleton filter;
        private AutoCompleteComboBoxListener<String> listener;


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            filter = FilterSingleton.getInstance();
            List<String> kilometers = new ArrayList<String>();
            for(int i =1; i <=20; i++){kilometers.add(String.valueOf(i*5));}
            choiceRadius.setItems(FXCollections.observableList(kilometers));
            choiceCity.setItems(getCities());
            listener = new AutoCompleteComboBoxListener<>(choiceCity);
            List<String> persons = new ArrayList<String>();
            for(int i = 1; i <=50; i++){persons.add(String.valueOf(i));}
            choiceMinPers.setItems(FXCollections.observableList(persons));
            sortBy.setItems(FXCollections.observableArrayList("price ascending", "price descending", "date - latest first", "date - earliest first", "company name reverse order", "company name in alphabetic order"));
            restoreFilter();
        }

    /**
     *Method that applies the filter.
     */
        public void applyFilter() {
            filter.filterItems();
        }

    /**
     *Method sets the value (city) to filter.
     */
        public void onValueChanged() {
            if (!choiceCity.getSelectionModel().isSelected(-1)) {
                choiceRadius.setDisable(false);
                filter.setCityValue(choiceCity.getValue());
            }
        }

    /**
     *Method that loads the list of cities.
     * @return an observable list of cities.
     */
        private ObservableList<String> getCities () {
            ObservableList<Location> locations = FXCollections.observableArrayList();
            ObservableList<String> cities = FXCollections.observableArrayList();
            EntityManager entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
            TypedQuery<Location> locationQuery;
            locationQuery = entityManager.createNamedQuery(
                    "Location.findAllLocation",
                    Location.class);
            locations.addAll(locationQuery.getResultList());
            for (Location city:locations) {
                if(!cities.contains(city.getCity())) {
                    cities.add(city.getCity());
                }
            }
            Comparator<String> comparator = Comparator.naturalOrder();
            FXCollections.sort(cities, comparator);
            return cities;
        }

    /**
     *Method sets the value (price) to filter.
     */
        public void onSliderChanged() {
            double price = choicePrice.getValue();
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            money.setText(numberFormat.format(price) + " €");
            filter.setPriceValue(choicePrice.getValue());

        }

    /**
     *Method sets the value (radius) to filter.
     */
        public void onRadiusChanged() {
            if (!choiceRadius.getSelectionModel().isSelected(-1)) {
                filter.setRadiusValue(Integer.valueOf(choiceRadius.getValue()));
                filter.setRadiusSelected(choiceRadius.getSelectionModel().getSelectedIndex());
            }
        }

    /**
     *Method sets the value (minimum nr. of fre places) to filter.
     */
        public void onPersonsChanged() {
            if (!choiceMinPers.getSelectionModel().isSelected(-1)) {
                filter.setMinPersValue(Integer.valueOf(choiceMinPers.getValue()));
                filter.setMinPersSelected(choiceMinPers.getSelectionModel().getSelectedIndex());
            }
        }

    /**
     *Method sorts the list in accordance with chosen option.
     */
        public void onSortingChanged(){
            filter.setSortSelected(sortBy.getSelectionModel().getSelectedIndex());
            filter.applySort();
        }

    /**
     * Methon that calls a key handler which filters the list in combobox.
     * @param keyEvent key pressed event.
     */
    public void keyReleased(KeyEvent keyEvent) {
            listener.handle(keyEvent);
    }

    /**
     *Method that resets the filter.
     */
    public  void resetFilter(){
        choiceCity.getSelectionModel().clearSelection();
        sortBy.getSelectionModel().clearSelection();
        choiceRadius.getSelectionModel().clearSelection();
        choiceMinPers.getSelectionModel().clearSelection();
        choicePrice.setValue(100.00);
        money.setText("100.00");
        choiceRadius.setDisable(true);
        filter.resetFilter();
    }

    /**
     *Method that restores the filter state.
     */
    public void restoreFilter(){

        choicePrice.setValue(filter.getPriceValue());
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        money.setText(numberFormat.format(filter.getPriceValue()) + " €");

        choiceCity.getSelectionModel().select(filter.getCityValue());
        choiceMinPers.getSelectionModel().select(filter.getMinPersSelected());
        choiceRadius.getSelectionModel().select(filter.getRadiusSelected());
        sortBy.getSelectionModel().select(filter.getSortSelected());
        if (!choiceCity.getSelectionModel().isSelected(-1)) {
            choiceRadius.setDisable(false);
        }
    }

}

