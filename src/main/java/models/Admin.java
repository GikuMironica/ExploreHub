package models;

import authentification.AdminConnectionSingleton;
import authentification.UserConnectionSingleton;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name= "Admin.findAdminByEmailPass", query =	"SELECT u FROM Admin u WHERE u.Email = :email AND u.Password = :password"),
        @NamedQuery(name= "Admin.findAdminbyEmail", query =	"SELECT u FROM Admin u WHERE u.Email = :email"),
        @NamedQuery(name= "Admin.findAdmins", query = "SELECT a FROM Admin a WHERE a.Access = :access")
})


/**
 *Model class which represents the admin entity and encapsulates direct access to it
 *
 * @author: Gheorghe Mironica
 */
@Entity
@Table(name="users")
public class Admin implements Account{

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
    @JoinColumn(name = "CourseID")
    private Courses Course;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Transactions> Transactions;

    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="wishlist",
            joinColumns = { @JoinColumn(name="StudentID")},
            inverseJoinColumns = { @JoinColumn (name = "EventID")}
    )
    private List<Events> events;

    /**
     *Method to get the entity primary key
     */
    public int getId() {
        return Id;
    }

    public void setAccess(int access) {
        Access = access;
    }

    /**
     *Method to get the entity email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *Method to reset the entity email
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     *  Method to get the entity First Name
     */
    public String getFirstname() {
        return Firstname;
    }

    /**
     *Method to reset the entity's First Name
     */
    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    /**
     *  Method to get the entity's Last Name
     */
    public String getLastname() {
        return Lastname;
    }

    /**
     *Method to reset the entity's Last Name
     */
    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    /**
     *Method to get the entity's access level
     */
    public int getAccess() {
        return Access;
    }

    /**
     *Method to get the entity's password
     */
    public String getPassword() {
        return Password;
    }

    /**
     *  Method to reset the entity's password
     */
    public void setPassword(String password) {
        Password = password;
    }

    /**
     *Method to get the entity associated attributes
     *@return Course object if found
     */
    public Courses getCourse() {
        return Course;
    }

    /**
     *Method to set an entity's associated attributes
     *@param course Course object
     */
    public void setCourse(Courses course) {
        Course = course;
    }

    /**
     *Method to set an entity's associated attributes
     *@return List of Transactions
     */
    public List<models.Transactions> getTransactions() {
        return Transactions;
    }

    /**
     *Method to associate this entity with a list of transactions
     *@param transactions List of transactions
     */
    public void setTransactions(List<models.Transactions> transactions) {
        Transactions = transactions;
    }

    /**
     *Method to set an entity's associated attributes
     *@return  List of Event objects
     */
    public List<Events> getEvents() {
        return events;
    }

    /**
     *Method to associate entity with a list of event objects
     *@param events List type events
     */
    public void setEvents(List<Events> events) {
        this.events = events;
    }

    /**
     *Method to access the access the database interface as Administrator
     *@return Entity Manager
     */
    @Override
    public EntityManager getConnection() {
        AdminConnectionSingleton u1 = AdminConnectionSingleton.getInstance();
        return u1.getManager();
    }


}
