package models;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import java.sql.Date;

@SuppressWarnings("JpaQlInspection")
@NamedQuery(name="Excursion.findAllExcursions", query="SELECT e FROM Excursion e")

/**
 * Model class which represents the Excursion Entity and encapsulates direct access to it
 *
 * @author Gheorghe Mironica
 */
@Entity
@DiscriminatorValue(value="EXCURSION")
public class Excursion extends Events {

    /**
     * Default constructor
     */
    public Excursion(){

    }

    /**
     * Custom Constructor
     * @param date {@link Date} input param
     * @param Price {@link Double} input param
     * @param totalPlaces {@link Integer} input param
     * @param availablePlaces {@link Integer} input param
     * @param shortDescription {@link String} input param
     * @param longDescription {@link String} input param
     */
    public Excursion(java.sql.Date date, Double Price, int totalPlaces, int availablePlaces, String shortDescription, String longDescription) {
        super(date, totalPlaces, availablePlaces, shortDescription, longDescription);
        this.Price = Price;
    }

    protected Double Price;

    /**
     * Method which returns the price of this event
     * @return {@link Double} price
     */
    @Override
    public Double getPrice() {
        return this.Price;
    }

    /**
     * Method which sets the price of this event
     * @param Price  {@link Double} price
     */
    @Override
    public void setPrice(Double Price){
        this.Price = Price;
    }

    /**
     * Method which returns the name of this event
     * Since it's a Hochschule Excursion
     * The event organizer is fixed to be hochschule
     * @return {@link String} Name of the university
     */
    @Override
    public String getCompany() {
        return "Hochschule Ulm";
    }

}
