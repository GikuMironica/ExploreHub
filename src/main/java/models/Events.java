package models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Events.findAllEvents", query="SELECT e FROM Events e"),
        @NamedQuery(name="Events.findAllValidEvents", query="SELECT e FROM Events e WHERE e.Date > :today"),
        @NamedQuery(name="Events.findMaxId", query="SELECT MAX(e.Id) FROM Events e")
})


/**
 *Model class which represents the Event entity and encapsulates direct access to it
 *
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
     * @param date {@link Date}
     * @param totalPlaces {@link Integer}
     * @param availablePlaces {@link Integer}
     * @param shortDescription {@link String}
     * @param longDescription {@link String}
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int Id;

    @Basic(optional=false)
    protected Date Date;

    @Basic(optional=false)
    protected int TotalPlaces;

    @Basic(optional=false)
    protected int AvailablePlaces;

    @Transient
    protected Query query;

    @Column(nullable = true, name="ShortDescription")
    protected String ShortDescription;

    @Column(nullable = true, name="LongDescription")
    protected String LongDescription;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="Id")
    protected Location location;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="Id")
    protected Pictures picture;

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

    abstract public String getCompany();

    public void setCompany(String company) {
      //
    }

    abstract public Double getPrice();

    public void setPrice(Double price){
        //
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Pictures getPicture() {
        return picture;
    }

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

}
