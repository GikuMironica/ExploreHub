package controlPanelComponent;

import models.CompanyExcursion;
import models.Events;
import models.Excursion;

import java.sql.Date;

/**
 * Concrete Strategy to create CompanyExcursions {@link CompanyExcursion}
 *
 * @author Gheorghe Mironica
 */
public class CompanyExcursionStrategy implements EventStrategy {

    private CompanyExcursion companyExcursion;

    /**
     * Custom Constructor
     * @param date {@link Date}
     * @param Company {@link String}
     * @param totalPlaces {@link Integer}
     * @param availablePlaces {@link Integer}
     * @param shortDescription {@link String}
     * @param longDescription {@link String}
     */
    public CompanyExcursionStrategy(java.sql.Date date, String Company, int totalPlaces, int availablePlaces, String shortDescription, String longDescription){
        companyExcursion = new CompanyExcursion(date, Company, totalPlaces, availablePlaces, shortDescription, longDescription);
    }

    /**
     * Concrete strategy to create CompanyExcursions
     * @return {@link CompanyExcursion}
     */
    @Override
    public Events createEvent() {
        return companyExcursion;
    }
}
