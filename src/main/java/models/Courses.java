package models;

import javax.persistence.*;
import java.util.Objects;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name="Courses.findCourses", query="SELECT c FROM Courses c WHERE c.Name NOT LIKE '%None%'"),
        @NamedQuery(name="Courses.findCourseByName", query="SELECT c FROM Courses c WHERE C.Name = :name")})

/**
 *Model class which represents the Course entity and encapsulates direct access to it
 * @author Gheorghe Mironica
 */
@Entity
@Table(name="courses")
public class Courses {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    private String Name;

    public Courses(){

    }
    public Courses(int id, String Name){
        this.Id = id;
        this.Name = Name;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Courses courses = (Courses) o;
        return Id == courses.Id &&
                Objects.equals(Name, courses.Name);
    }

}
