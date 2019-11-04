package models;

import javax.persistence.*;

@Entity
@Table(name="location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int EventID;

    @Basic(optional=false)
    private double Xcoord;

    @Basic(optional=false)
    private double Ycoord;

    @Basic(optional=false)
    private String City;

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public double getXcoord() {
        return Xcoord;
    }

    public void setXcoord(double xcoord) {
        Xcoord = xcoord;
    }

    public double getYcoord() {
        return Ycoord;
    }

    public void setYcoord(double ycoord) {
        Ycoord = ycoord;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
