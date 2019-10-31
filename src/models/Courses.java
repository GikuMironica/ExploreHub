package models;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name="Courses.findCourses", query="SELECT c FROM Courses c"),
        @NamedQuery(name="Courses.findCourseByName", query="SELECT c FROM Courses c WHERE C.Name = :name")})
@Entity
@Table(name="courses")
public class Courses {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
