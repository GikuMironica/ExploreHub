package models;

import javax.persistence.*;

@Entity
@Table(name="pictures")
public class Pictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int EventID;

    @Basic(optional=false)
    private String Logo;

    @Basic(optional=false)
    private String Picture;

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
