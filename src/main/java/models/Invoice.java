package models;

import javax.persistence.*;
import java.sql.Date;

/**
 * Model class which encapsulates the data of the Invoice entity and the logic to manage it
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("JpaQlInspection")
@NamedQuery(name="Invoice.findAllInvoicesForUser", query="SELECT i FROM Invoice i WHERE i.Customer =:user")

@Entity
@Table(name="invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    @Column(name="CustomerName")
    private String Customer;

    @Column(name="Date")
    private Date EventDate;

    @Basic(optional = false)
    private Double Ammount;

    @Basic(optional=false)
    private String EventName;

    @Basic(optional=false)
    private String Company;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="TransactionID")
    private Transactions TransactionID;

    /**
     * Default Contructor
     */
    public Invoice(){
        // ctor
    }

    /**
     * Custom Constructor
     *
     * @param transaction {@link Transactions} as input parameter
     */
    public Invoice(Transactions transaction){
        this.Customer = transaction.getUser().getLastname();
        this.EventDate = transaction.getDate();
        this.Ammount = transaction.getEvent().getPrice();
        this.EventName = transaction.getEvent().getShortDescription();
        this.Company = transaction.getEvent().getCompany();
        this.TransactionID = transaction;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Transactions getTransaction() {
        return TransactionID;
    }

    public void setTransaction(Transactions transactions) {
        this.TransactionID = transactions;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public Date getEventDate() {
        return EventDate;
    }

    public void setEventDate(Date eventDate) {
        EventDate = eventDate;
    }

    public Double getAmmount() {
        return Ammount;
    }

    public void setAmmount(Double ammount) {
        Ammount = ammount;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

}
