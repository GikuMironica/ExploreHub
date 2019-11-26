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
     * @param date {@link Date}
     * @param Price {@link Double}
     * @param totalPlaces {@link Integer}
     * @param availablePlaces {@link Integer}
     * @param shortDescription {@link String}
     * @param longDescription {@link String}
     */
    public Excursion(java.sql.Date date, Double Price, int totalPlaces, int availablePlaces, String shortDescription, String longDescription) {
        super(date, totalPlaces, availablePlaces, shortDescription, longDescription);
        this.Price = Price;
    }

    protected Double Price;

    @Override
    public Double getPrice() {
        return this.Price;
    }

    @Override
    public void setPrice(Double Price){
        this.Price = Price;
    }

    @Override
    public String getCompany() {
        return "Hochschule Ulm";
    }

}
