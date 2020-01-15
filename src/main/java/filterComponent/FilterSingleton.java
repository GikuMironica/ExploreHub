package filterComponent;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import listComponent.EventListSingleton;
import models.Events;

import java.util.Comparator;


/**
 * Class that implements the filter.
 *
 * @author Aleksejs Marmiss
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
    private String searchKeyword = "";
    private EventListSingleton listSingleton;

    /**
     * Getter for city.
     *
     * @return city name as String.
     */
    public String getCityValue() {
        return cityValue;
    }

    /**
     * Getter for radius.
     *
     * @return radius as integer.
     */
    public int getRadiusValue() {
        return radiusValue;
    }

    /**
     * Getter for minimum number of free places.
     *
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
     *
     * @return index of previously selected item.
     */
    public int getRadiusSelected() {
        return radiusSelected;
    }

    /**
     * Getter for getting the index of previously selected entry in "minimum nr. of free place" ComboBox.
     *
     * @return index of previously selected item.
     */
    public int getMinPersSelected() {
        return minPersSelected;
    }

    /**
     * Getter for getting the index of previously selected entry in "Sort by" ComboBox.
     *
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
     *
     * @return unfiltered Observable list.
     */
    public ObservableList<Events> getBackup() {
        return backup;
    }

    /**
     * Getter for Filter Singleton instance.
     *
     * @return instance of the FilterSingleton.
     */
    public static FilterSingleton getInstance() {
        return ourInstance;
    }

    /**
     * Setter for setting the city name for filter.
     *
     * @param cityValue name of the city.
     */
    public void setCityValue(String cityValue) {
        this.cityValue = cityValue;
    }

    /**
     * Setter for setting the radius for filter.
     *
     * @param radiusValue radius in kilometers as integer.
     */
    public void setRadiusValue(int radiusValue) {
        this.radiusValue = radiusValue;
    }

    /**
     * Setter for setting the minimum nr. of free places for filter.
     *
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
     *
     * @return maximum price as double.
     */
    public double getPriceValue() {
        return priceValue;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
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
    public void filterItems() {
        Criteria radius = new RadiusCriteria(radiusValue, cityValue);
        Criteria minPers = new FreePlacesCriteria(minPersValue);
        Criteria price = new PriceCriteria(priceValue);
        Criteria searchCriteria = new SearchCriteria(searchKeyword);

        Criteria filter = new AndCriteria(minPers, price);
        filter = new AndCriteria(searchCriteria, filter);
        Criteria totalFilter = new AndCriteria(radius, filter);
        ObservableList<Events> listValues = listSingleton.getEventsObservableList();
        listValues.clear();
        listValues.addAll(totalFilter.meetCriteria(backup));
        applySort();
    }

    /**
     * Methos that resets the filter
     */
    public void resetFilter() {
        ObservableList<Events> listValues = listSingleton.getEventsObservableList();
        listValues.clear();
        listValues.addAll(backup);
        priceValue = 100;
        radiusSelected = -1;
        minPersSelected = -1;
        sortSelected = -1;
        cityValue = null;
        searchKeyword = "";
        minPersValue = 0;
        radiusValue = 0;
    }

    /**
     * Method that updates the backup of ObservableList.
     */
    public void updateFilter() {
        backup = FXCollections.observableArrayList(listSingleton.getEventsObservableList());
        filterItems();
    }

    public void applySort() {
        if (sortSelected != -1) {
            EventListSingleton listSingleton = EventListSingleton.getInstance();
            ObservableList<Events> toSortList = listSingleton.getEventsObservableList();
            if (sortSelected == 0) toSortList.sort(Comparator.comparingDouble(Events::getPrice));
            else if (sortSelected == 1) toSortList.sort(Comparator.comparingDouble(Events::getPrice).reversed());
            else if (sortSelected == 2) toSortList.sort(Comparator.comparing(Events::getDate).reversed());
            else if (sortSelected == 3) toSortList.sort(Comparator.comparing(Events::getDate));
            else if (sortSelected == 4) toSortList.sort(Comparator.comparing(Events::getCompany).reversed());
            else if (sortSelected == 5) toSortList.sort(Comparator.comparing(Events::getCompany));
        }
    }
}
