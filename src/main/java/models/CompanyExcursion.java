package models;
import javax.persistence.*;
import java.sql.Date;

@SuppressWarnings("JpaQlInspection")
@NamedQuery(name="CompanyExcursion.findAllCExcursions", query="SELECT e FROM CompanyExcursion e")

/**
 * Model class which represents the CompanyExcursion Entity and encapsulates direct access to it
 *
 * @author Gheorghe Mironica
 */
@Entity
@DiscriminatorValue(value="COMPANY_EXCURSION")
public class CompanyExcursion extends Events {

    /**
     * Default constructor
     */
    public CompanyExcursion(){

    }

    /**
     * Custom Constructor
     * @param date {@link Date}
     * @param company {@link String}
     * @param totalPlaces {@link Integer}
     * @param availablePlaces {@link Integer}
     * @param shortDescription {@link String}
     * @param longDescription {@link String}
     */
    public CompanyExcursion(java.sql.Date date, String company, int totalPlaces, int availablePlaces, String shortDescription, String longDescription) {
        super(date, totalPlaces, availablePlaces, shortDescription, longDescription);
        this.Company = company;
    }

    @Column(length=45)
    @Basic(optional=false)
    private String Company;

    /**
     * Method which returns the price of the event
     * Since it's a company excursion,
     * like BMW excursion, Daimler excursion
     * price it's fixed to be 0.
     * @return {@link Double} price
     */
    @Override
    public Double getPrice() {
        return 0.0;
    }

    /**
     * Method which returns the company name,
     * which organizes the excursion
     * @return {@link String} company name
     */
    @Override
    public String getCompany() {
        return this.Company;
    }

    /**
     * Method which sets the company name,
     * which organizes the excursion
     * @param company  {@link String} company name
     */
    @Override
    public void setCompany(String company) {
        Company = company;
    }

}