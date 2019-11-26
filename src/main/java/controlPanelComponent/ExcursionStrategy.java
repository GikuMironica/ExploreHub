package controlPanelComponent;

import models.CompanyExcursion;
import models.Events;
import models.Excursion;

import java.sql.Date;

/**
 * Concrete Strategy to create CompanyExcursions {@link Excursion}
 *
 * @author Gheorghe Mironica
 */
public class ExcursionStrategy implements EventStrategy {

    private Excursion excursion;

    /**
     * Constructor for building Excursion
     *
     * @param date {@link Date}
     * @param price {@link Double}
     * @param totalPlaces {@link Integer}
     * @param availablePlaces {@link Integer}
     * @param shortDescription {@link String}
     * @param longDescription {@link String}
     */
    public ExcursionStrategy(java.sql.Date date, Double price, int totalPlaces, int availablePlaces, String shortDescription,
                             String longDescription){
        excursion = new Excursion(date, price, totalPlaces, availablePlaces, shortDescription, longDescription);
    }

    /**
     * Concrete strategy to create Excursions
     * @return {@link Excursion}
     */
    @Override
    public Excursion createEvent() {
        return excursion;
    }


}
