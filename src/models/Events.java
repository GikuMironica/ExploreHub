package models;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name="event")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    private Date Date;

    @Basic(optional=false)
    private String Company;

    @Basic(optional=false)
    private Double Price;

    @Basic(optional=false)
    private int TotalPlaces;

    @Basic(optional=false)
    private int AvailablePlaces;

    @Column(nullable = true, name="ShortDescription")
    private String ShortDescription;

    @Column(nullable = true, name="LongDescription")
    private String LongDescription;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public int getTotalPlaces() {
        return TotalPlaces;
    }

    public void setTotalPlaces(int totalPlaces) {
        TotalPlaces = totalPlaces;
    }

    public int getAvailablePlaces() {
        return AvailablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        AvailablePlaces = availablePlaces;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }
}
