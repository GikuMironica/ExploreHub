package models;

import javax.persistence.*;

@Entity
@Table(name="courses")
public class Courses {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int Id;

    @Basic(optional=false)
    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
