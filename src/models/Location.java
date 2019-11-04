package models;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Location.findAllLocation", query="SELECT l FROM Location l")
})

@Entity
@Table(name="location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int EventID;

    @Basic(optional=false)
    private double Latitude;

    @Basic(optional=false)
    private double Longitude;

    @Basic(optional=false)
    private String City;

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
