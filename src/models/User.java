package models;

import authentification.UserConnectionSingleton;

import javax.persistence.*;
import java.util.List;
import java.util.Set;



@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name= "User.findUserbyEmailPass", query =	"SELECT u FROM User u WHERE u.Email = :email AND u.Password = :password"),
        @NamedQuery(name= "User.findUserbyEmail", query =	"SELECT u FROM User u WHERE u.Email = :email"),
        @NamedQuery(name= "User.determineAccess", query = "SELECT u.Access FROM User u WHERE u.Email = :email AND u.Password = :password")
})


@Entity
@Table(name="users")
public class User implements Account{

    public User(){

    }
    public User(String firstname, String lastname, String email, String password, Courses course) {
        this.Email = email;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Password = password;
        this.Course = course;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Basic(optional=false)
    private int Id;

    @Basic(optional=false)
    private String Email;

    @Basic(optional=false)
    @Column(name="FirstName")
    private String Firstname;

    @Basic(optional=false)
    @Column(name="LastName")
    private String Lastname;

    @Basic(optional=false)
    @Column(name="AccessLevel")
    private int Access;

    @Basic(optional=false)
    private String Password;

    @ManyToOne
    @JoinColumn(name = "CourseID", nullable=true)
    private Courses Course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Transactions> Transactions;

    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="wishlist",
        joinColumns = { @JoinColumn(name="StudentID")},
            inverseJoinColumns = { @JoinColumn (name = "EventID")}
            )
    private List<Events> events;

    public int getId() {
        return Id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public int getAccess() {
        return Access;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Courses getCourse() {
        return Course;
    }

    public void setCourse(Courses course) {
        Course = course;
    }

    public List<models.Transactions> getTransactions() {
        return Transactions;
    }

    public void setTransactions(List<models.Transactions> transactions) {
        Transactions = transactions;
    }

    public List<Events> getEvents() {
        return events;
    }

    public void setEvents(List<Events> events) {
        this.events = events;
    }

    @Override
    public EntityManager getConnection() {
        UserConnectionSingleton u1 = UserConnectionSingleton.getInstance();
        return u1.getManager();
    }

}
