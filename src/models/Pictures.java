package models;

import javax.persistence.*;

@Entity
@Table(name="pictures")
public class Pictures {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int EventID;

    @Basic(optional=false)
    private String Logo;

    @Basic(optional=false)
    private String Picture;

    /**
     * Default constructor
     */
    Pictures(){

    }

    /**
     * Constructor
     *
     * @param logo url for the log
     * @param picture url for the main picture
     */
    public Pictures(String logo, String picture) {
        Logo = logo;
        Picture = picture;
    }


    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}
