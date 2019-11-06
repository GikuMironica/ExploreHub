package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import listComponent.EventListSingleton;
import models.Events;

/**
 *Class that implemets the filter.
 * @author Aleksejs Marmiss
 *
 */
public class FilterSingleton {
    private static FilterSingleton ourInstance = new FilterSingleton();
    private double priceValue = 100;
    private ObservableList<Events> backup;
    private String cityValue = null;
    private int radiusValue = 0;
    private int minPersValue = 0;
    private int radiusSelected = -1;
    private int minPersSelected = -1;
    private int sortSelected = -1;
    private EventListSingleton listSingleton;

    /**
     * Getter for city.
     * @return city name as String.
     */
    public String getCityValue() {
        return cityValue;
    }
    /**
     * Getter for radius.
     * @return radius as integer.
     */
    public int getRadiusValue() {
        return radiusValue;
    }
    /**
     * Getter for minimum number of free places.
     * @return radius as integer.
     */
    public int getMinPersValue() {
        return minPersValue;
    }

    /**
     * Setter for storing the index of selected entry in "Sort" ComboBox.
     */
    public void setSortSelected(int sortSelected) {
        this.sortSelected = sortSelected;
    }
    /**
     * Getter for getting the index of previously selected entry in "Radius" ComboBox.
     * @return index of previously selected item.
     */
    public int getRadiusSelected() {
        return radiusSelected;
    }
    /**
     * Getter for getting the index of previously selected entry in "minimum nr. of free place" ComboBox.
     * @return index of previously selected item.
     */
    public int getMinPersSelected() {
        return minPersSelected;
    }
    /**
     * Getter for getting the index of previously selected entry in "Sort by" ComboBox.
     * @return index of previously selected item.
     */
    public int getSortSelected() {
        return sortSelected;
    }

    /**
     * Setter for storing the index of selected entry in "Radius" ComboBox.
     */
    public void setRadiusSelected(int radiusSelected) {
        this.radiusSelected = radiusSelected;
    }
    /**
     * Setter for storing the index of selected entry in "minimum nr. of free place" ComboBox.
     */
    public void setMinPersSelected(int minPersSelected) {
        this.minPersSelected = minPersSelected;
    }
    /**
     * Getter for getting the unfiltered list.
     * @return unfiltered Observable list.
     */
    public ObservableList<Events> getBackup() {
        return backup;
    }

    /**
     * Getter for Filter Singleton instance.
     * @return
     */
    public static FilterSingleton getInstance() {
        return ourInstance;
    }

    /**
     * Setter for setting the city name for filter.
     * @param cityValue name of the city.
     */
    public void setCityValue(String cityValue) {
        this.cityValue = cityValue;
    }

    /**
     * Setter for setting the radius for filter.
     * @param radiusValue radius in kilometers as integer.
     */
    public void setRadiusValue(int radiusValue) {
        this.radiusValue = radiusValue;
    }

    /**
     * Setter for setting the minimum nr. of free places for filter.
     * @param minPersValue minimum number of free places as integer.
     */
    public void setMinPersValue(int minPersValue) {
        this.minPersValue = minPersValue;
    }
    /**
     * Setter for maximum price value.
     */
    public void setPriceValue(double priceValue) {
        this.priceValue = priceValue;
    }

    /**
     * Getter for maximum price value.
     * @return maximum price as double.
     */
    public double getPriceValue() {
        return priceValue;
    }

    /**
     * Constructor for Filter Singleton.
     */
    private FilterSingleton() {
        listSingleton = EventListSingleton.getInstance();
        backup = FXCollections.observableArrayList(listSingleton.getEventsObservableList());

    }

    /**
     * Methods that creates and applies criteria to filter the list.
     */
    public void filterItems(){
        Criteria radius = new RadiusCriteria(radiusValue, cityValue);
        Criteria minPers = new FreePlacesCriteria(minPersValue);
        Criteria price = new PriceCriteria(priceValue);

        Criteria filter = new AndCriteria(minPers,price);
        Criteria totalFilter = new AndCriteria(radius,filter);
        ObservableList<Events> listValues = listSingleton.getEventsObservableList();
        int i = 0;
        while ( i < listValues.size()){
            listValues.remove(i);
        }
        listValues.addAll(totalFilter.meetCriteria(backup));
    }

    /**
     * Methos that resets the filter
     */
    public void resetFilter(){
        ObservableList<Events> listValues = listSingleton.getEventsObservableList();
        int i = 0;
        while ( i < listValues.size()){
            listValues.remove(i);
        }
        listValues.addAll(backup);
        priceValue = 100;
        radiusSelected = -1;
        minPersSelected = -1;
        sortSelected = -1;
        cityValue = null;
    }


}
