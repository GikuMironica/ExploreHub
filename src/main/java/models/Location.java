package models;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Location.findAllLocation", query="SELECT l FROM Location l")
})

/**
 * Model class which encapsulates the data of the Locaton entity and the logic to manage it
 *
 * @author Gheorghe Mironica
 */
@Entity
@Table(name="location")
public class Location {

    @Id
    @Column(length=4)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int EventID;

    @Basic(optional=false)
    private double Latitude;

    @Basic(optional=false)
    private double Longitude;

    @Column(length=45)
    @Basic(optional=false)
    private String City;

    /**
     * Default constructor
     */
    Location(){

    }

    /**
     * Constructor
     * @param id id of the {@link Events}
     * @param latitude latitude {@link String}
     * @param longitude longitude {@link String}
     * @param city city name {@link String}
     */
    public Location(double latitude, double longitude, String city) {
        Latitude = latitude;
        Longitude = longitude;
        City = city;
    }

    /** GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS*/

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double xcoord) {
        Latitude = xcoord;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double ycoord) {
        Longitude = ycoord;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
