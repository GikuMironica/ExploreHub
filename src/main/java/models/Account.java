package models;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name = "Account.determineAccess", query = "SELECT u.Access FROM Account u WHERE u.Email = :email AND u.Password = :password"),
        @NamedQuery(name = "Account.findAccountByEmail", query = "SELECT a FROM Account a WHERE a.Email =:email"),
        @NamedQuery(name = "Account.getStatusById", query="SELECT a.Active FROM Account a WHERE a.Id = :Id"),
        @NamedQuery(name = "Account.findNrActive", query="SELECT COUNT(a) FROM Account a WHERE a.Active = 1")
})

@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AccessLevel")
public abstract class Account{

    /**
     * Default constructor
     */
    public Account(){

    }

    @Transient
    protected Query query;

    @Id
    @Column(length=5)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Basic(optional=false)
    protected int Id;

    @Column(length=45)
    @Basic(optional=false)
    protected String Email;

    @Basic(optional=false)
    @Column(name="FirstName", length = 45)
    protected String Firstname;

    @Basic(optional=false)
    @Column(name="LastName", length = 45)
    protected String Lastname;

    @Basic(optional=false)
    @Column(name="AccessLevel", length = 1)
    protected int Access;

    @Basic(optional=false)
    @Column(length = 1)
    protected int Active;

    @Transient
    protected List<Events> bookedEvents;

    @Basic(optional=false)
    @Column(length=45)
    protected String Password;

    @Column(name="Picture", length = 45)
    protected String picture;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    protected Courses Course;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    protected List<Transactions> Transactions;

    @OneToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="wishlist",
            joinColumns = { @JoinColumn(name="StudentID")},
            inverseJoinColumns = { @JoinColumn (name = "EventID")}
    )
    protected List<Events> events;

    public Account(String firstname, String lastname, String email, String password, Courses course) {
        this.Email = email;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Password = password;
        this.Course = course;
    }

    public Account(String firstname, String lastname, String email, String password, Courses course, String picture) {
        this(firstname, lastname, email, password, course);
        this.picture = picture;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int active) {
        Active = active;
    }

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<models.Events> getBookedEvents() {
        return bookedEvents;
    }

    public void setBookedEvents(List<models.Events> bookedEvents) {
        this.bookedEvents = bookedEvents;
    }

    /**
     * Method to access the access the database interface as User
     * @return Entity Manager
     */
    public abstract EntityManager getConnection();

    /**
     * This method closes connection to db
     */

    public abstract void closeConnection();

    @SuppressWarnings("JpaQueryApiInspection")
    public boolean checkEventPresence(EntityManager em, int eventID){
        query = em.createNamedQuery("checkIfEventInWishList");
        query.setParameter(1, getId());
        query.setParameter(2, eventID);
        int i = ((Number) query.getSingleResult()).intValue();
        return !(i==0);
    }
}