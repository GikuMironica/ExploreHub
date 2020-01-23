package models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Events.findAllEvents", query="SELECT e FROM Events e"),
        @NamedQuery(name="Events.findAllValidEvents", query="SELECT e FROM Events e WHERE e.Date > :today"),
        @NamedQuery(name="Events.findMaxId", query="SELECT MAX(e.Id) FROM Events e")
})


/**
 * Model class which encapsulates the data of the Events entity and the logic to manage it
 * @author Gheorghe Mironica
 */
@Entity
@Table(name="event")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EVENT_TYPE")
public abstract class Events implements Serializable {

    /**
     * Default constructor
     */
    public Events(){

    }

    /**
     * Custom Constructor
     * @param date {@link Date} event date
     * @param totalPlaces {@link Integer} total places
     * @param availablePlaces {@link Integer} available places
     * @param shortDescription {@link String} title
     * @param longDescription {@link String} description
     */
    public Events(java.sql.Date date, int totalPlaces, int availablePlaces, String shortDescription,
                  String longDescription) {
        Date = date;
        TotalPlaces = totalPlaces;
        AvailablePlaces = availablePlaces;
        ShortDescription = shortDescription;
        LongDescription = longDescription;
        this.location = location;
        this.picture = picture;
    }

    @Id
    @Column(length=4)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int Id;

    @Basic(optional=false)
    protected Date Date;

    @Column(length=2)
    @Basic(optional=false)
    protected int TotalPlaces;

    @Column(length=2)
    @Basic(optional=false)
    protected int AvailablePlaces;

    @Transient
    protected Query query;

    @Column(nullable = true, name="ShortDescription", length = 60)
    protected String ShortDescription;

    @Column(nullable = true, name="LongDescription")
    protected String LongDescription;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="Id")
    protected Location location;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="Id")
    protected Pictures picture;

    /**
     * Event id getter
     * @return {@link Integer} id
     */
    public int getId() {
        return Id;
    }

    /**
     * Event id setter
     */
    public void setId(int id) {
        Id = id;
    }

    /**
     * Event date getter
     * @return {@link Date} event date
     */
    public Date getDate() {
        return Date;
    }

    /**
     * Event date setter
     * @param {@link Date} event date
     */
    public void setDate(Date date) {
        Date = date;
    }

    /**
     * Event company name getter
     * Abstract, implemented in Company Excursion
     * In hochschule excursion is final
     * @return {@link String} company name
     */
    abstract public String getCompany();

    /**
     * Event company name setter
     * @param company {@link String} name
     */
    public void setCompany(String company) {
      //
    }

    /**
     * Event price getter
     * Abstract method, implemented in Hochschule Excursions since it's a paid excursion
     * @return {@link Double} price
     */
    abstract public Double getPrice();

    /**
     * Event price setter
     * @param price {@link Double} number of places
     */
    public void setPrice(Double price){
        //
    }

    /**
     * Total place getter
     * @return {@link Integer} number of places
     */
    public int getTotalPlaces() {
        return TotalPlaces;
    }

    /**
     * Total place setter
     * @param totalPlaces  {@link Integer} number of places
     */
    public void setTotalPlaces(int totalPlaces) {
        TotalPlaces = totalPlaces;
    }

    /**
     * Event available places getter
     * @return {@link Integer} number of places
     */
    public int getAvailablePlaces() {
        return AvailablePlaces;
    }

    /**
     * Event avaialble places setter
     * @param availablePlaces  {@link Integer} number of places
     */
    public void setAvailablePlaces(int availablePlaces) {
        AvailablePlaces = availablePlaces;
    }

    /**
     * Event title getteer
     * @return {@link String} title
     */
    public String getShortDescription() {
        return ShortDescription;
    }

    /**
     * Event title setter
     * @param shortDescription {@link String} title
     */
    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    /**
     * Event description getter
     * @return {@link String} description
     */
    public String getLongDescription() {
        return LongDescription;
    }

    /**
     * Long description setter
     * @param longDescription  {@link String} long description
     */
    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    /**
     * Event location getter
     *
     * @return {@link Location} Location object, consists of few attribute like City, Lat, Long
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Event location setter
     * @return {@link Location) location object
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Event picture getter
     * @return {@link Pictures} returns picture object, consists of Logo, Main picture
     */
    public Pictures getPicture() {
        return picture;
    }

    /**
     * Event picture setter
     * @param picture  {@link Pictures} parameter is type Pictures.
     */
    public void setPicture(Pictures picture) {
        this.picture = picture;
    }

    /**
     *  Method which count's how many accounts added this event to wishlist
     * @param em Entity Manager must be passed by reference
     * @param id The event ID
     * @return returns the counter
     */
    public final int getCheckedIN(EntityManager em, int id) {
        query = em.createNativeQuery("SELECT COUNT(*) FROM wishlist WHERE EventID = ?");
        query.setParameter(1, id);

        return ((Number) query.getSingleResult()).intValue();
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Events){
            return ((Events)(obj)).getId() == this.getId();
        }

        return false;
    }
}
