package models;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *Model class which represents the Transaction entity and encapsulates direct access to it
 *
 * @author Gheorghe Mironica
 */

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name="Transactions.findAllActiveTransactions", query = "SELECT t FROM Transactions t WHERE t.Completed=0"),
        @NamedQuery(name="Transactions.findAllProcessedTransactions", query="SELECT t FROM Transactions t WHERE t.Completed=1 OR t.Completed=2 OR t.Completed=3"),
        @NamedQuery(name="Transactions.findAllTransactions", query="SELECT t FROM Transactions t"),
        @NamedQuery(name="Transactions.findAllOngoing&Accepted", query="SELECT t FROM Transactions t " +
                "WHERE t.user.Id =:userId AND t.event.Id =:id " +
                "AND (t.Completed = 0 OR t.Completed = 1)")
})

@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    private Date Date;

    @Basic(optional=false)
    private int Completed;

    @Basic(optional=false)
    @Column(name="PaymentMethod")
    private int PaymentMethod;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="EventID", nullable=false)
    private Events event;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "StudentID", nullable=false)
    private User user;

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
