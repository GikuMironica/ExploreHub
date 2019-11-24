package models;

<<<<<<< HEAD
import javax.persistence.EntityManager;
=======
import javax.persistence.*;
>>>>>>> [DiscussionComponent] UI implemented
import java.util.List;

/**
 * Interface for the {@link User} , {@link Admin}
 *
 * @author Gheorghe Mironica
 * */
<<<<<<< HEAD
public interface Account {

    int getId();

    /**
     *  Method to get the entity email
     */
    String getEmail();

    /**
     *  Method to reset the entity email
     */
    void setEmail(String email);

    /**
     *  Method to get the entity First Name
     */
    String getFirstname();

    /**
     *  Method to reset the entity's First Name
     */
    void setFirstname(String firstname);

    /**
     *  Method to get the entity's Last Name
     */
    String getLastname();

    /**
     *  Method to reset the entity's Last Name
     */
    void setLastname(String lastname);

    /**
     *  Method to get the entity's access level
     */
    int getAccess();

    /**
     *  Method to get the entity's password
     */
    String getPassword();

    /**
     *  Method to reset the entity's password
     */
    void setPassword(String password);

    /**
     *  Method to get the entity associated attribute
     * @return Course object if found
     */
    Courses getCourse();

    /**
     *  Method to set an entity's associated attributes
     * @param course Course object
     */
    void setCourse(Courses course);

    /**
     *  Method to set an entity's associated attributes
     * @return List of Transactions
     */
    List<Transactions> getTransactions();

    /**
     *  Method to associate this entity with a list of transactions
     * @param transactions List of transactions
     */
    void setTransactions(List<models.Transactions> transactions);

    /**
     *  Method to set an entity's associated attributes
     * @return  List of Event objects
     */
    List<Events> getEvents();

    /**
     *  Method to associate entity with a list of event objects
     * @param events List type events
     */
    void setEvents(List<Events> events);

    List<models.Events> getBookedEvents();

    void setBookedEvents(List<models.Events> bookedEvents);

    /**
     *  Method to access the access the database interface as User
     * @return Entity Manager
     */
    EntityManager getConnection();

    void closeConnection();
=======
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name="users")
public abstract class Account {
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

    @Transient
    private List<Events> bookedEvents;

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

    public EntityManager getConnection() {
       return null;
    }

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

    public void setAccess(int access) {
        Access = access;
    }

    public List<Events> getBookedEvents() {
        return bookedEvents;
    }

    public void setBookedEvents(List<Events> bookedEvents) {
        this.bookedEvents = bookedEvents;
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
>>>>>>> [DiscussionComponent] UI implemented
}
