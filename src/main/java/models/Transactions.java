package models;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
	    @NamedQuery(name= "Transactions.findTransactionsById", query =	"SELECT t FROM Transactions t WHERE t.Id = :id "),
        @NamedQuery(name= "Transactions.findTransactionsByUserId", query =	"SELECT t FROM Transactions t WHERE t.user.Id = :id "),
        @NamedQuery(name="Transactions.findAllActiveTransactions", query = "SELECT t FROM Transactions t WHERE t.Completed=0"),
        @NamedQuery(name="Transactions.findAllProcessedTransactions", query="SELECT t FROM Transactions t WHERE t.Completed=1 OR t.Completed=2 OR t.Completed=3"),
        @NamedQuery(name="Transactions.findAllTransactions", query="SELECT t FROM Transactions t"),
        @NamedQuery(name="Transactions.findAllOngoing&Accepted", query="SELECT t FROM Transactions t " +
                "WHERE t.user.Id =:userId AND t.event.Id =:id " +
                "AND (t.Completed = 0 OR t.Completed = 1)")
})

/**
 * Model class which represents the Transaction entity and encapsulates direct access to it
 *
 * @author Gheorghe Mironica
 */
@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @Column(length = 5)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    private Date Date;

    @Column(length = 1)
    @Basic(optional=false)
    private int Completed;

    @Basic(optional=false)
    @Column(name="PaymentMethod", length = 1)
    private int PaymentMethod;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="EventID", nullable=false)
    private Events event;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "StudentID", nullable=false)
    private User user;

    // one to one with invoice
    @OneToOne(mappedBy = "TransactionID", cascade = CascadeType.ALL)
    private Invoice invoice;

    /**
     * Default constructor
     */
    public Transactions(){

    }

    /**
     * Custom constructor
     * @param date {@link Date} date of the transaction
     * @param completed {@link Integer} status of the transaction
     * @param paymentMethod {@link Integer} chosen payment method
     * @param event {@link} {@link Events} booked event
     * @param user {@link User} user who booked the event
     */
    public Transactions(Date date, int completed, int paymentMethod, Events event, User user){
        this.Date = date;
        this.Completed = completed;
        this.PaymentMethod = paymentMethod;
        this.event = event;
        this.user = user;
    }

    /** GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS - GETTERS - SETTERS*/

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}
