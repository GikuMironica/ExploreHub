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
@NamedNativeQuery(name="checkIfEventInWishList", query ="SELECT Count(*) FROM wishlist WHERE StudentID = ? AND EventID = ?;")

/**
 *Model class which represents the user entity and encapsulates direct access to it
 *
 *@Author: Gheorghe Mironica
 */
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

    @Transient
    private Query query;

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

    /**
     *  Method to get the entity primary key
     */
    public int getId() {
        return Id;
    }

    /**
     *  Method to get the entity email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *  Method to reset the entity email
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
     *  Method to reset the entity's First Name
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
     *  Method to reset the entity's Last Name
     */
    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    /**
     *  Method to get the entity's access level
     */
    public int getAccess() {
        return Access;
    }

    /**
     *  Method to get the entity's password
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
     *  Method to get the entity associated attribute
     * @return Course object if found
     */
    public Courses getCourse() {
        return Course;
    }

    /**
     *  Method to set an entity's associated attributes
     * @param course Course object
     */
    public void setCourse(Courses course) {
        Course = course;
    }

    /**
     *  Method to set an entity's associated attributes
     * @return List of Transactions
     */
    public List<models.Transactions> getTransactions() {
        return Transactions;
    }

    /**
     *  Method to associate this entity with a list of transactions
     * @param transactions List of transactions
     */
    public void setTransactions(List<models.Transactions> transactions) {
        Transactions = transactions;
    }

    /**
     *  Method to set an entity's associated attributes
     * @return  List of Event objects
     */
    public List<Events> getEvents() {
        return events;
    }

    /**
     *  Method to associate entity with a list of event objects
     * @param events List type events
     */
    public void setEvents(List<Events> events) {
        this.events = events;
    }

    public List<models.Events> getBookedEvents() {
        return bookedEvents;
    }

    public void setBookedEvents(List<models.Events> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    /**
     *  Method to access the access the database interface as User
     * @return Entity Manager
     */
    @Override
    public EntityManager getConnection() {
        UserConnectionSingleton u1 = UserConnectionSingleton.getInstance();
        return u1.getManager();
    }

    /**
     * This method closes connection to db
     */
    @Override
    public void closeConnection(){
        UserConnectionSingleton.getInstance().closeConnection();
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public boolean checkEventPresence(EntityManager em, int eventID){
        query = em.createNamedQuery("checkIfEventInWishList");
        query.setParameter(1, getId());
        query.setParameter(2, eventID);
        int i = ((Number) query.getSingleResult()).intValue();
        if(i==0)
            return false;
        else
            return true;
    }
}
