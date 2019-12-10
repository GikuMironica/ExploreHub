package models;

import javax.persistence.*;
import java.sql.Date;

/**
 * Model class which encapsulates the data of the Invoice entity and the logic to manage it
 *
 * @author Gheorghe Mironica
 */
@Entity
@Table(name="invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Basic(optional=false)
    @Column(name="CustumerName")
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
    @JoinColumn(name="Id", nullable = false)
    private Transactions transaction;

    public Invoice(){
        // ctor
    }

    public Invoice(Transactions transaction){
        this.Id = transaction.getId();
        this.Customer = transaction.getUser().getLastname();
        this.EventDate = transaction.getDate();
        this.Ammount = transaction.getEvent().getPrice();
        this.EventName = transaction.getEvent().getShortDescription();
        this.Company = transaction.getEvent().getCompany();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public Transactions getTransaction() {
        return transaction;
    }

    public void setTransaction(Transactions transaction) {
        this.transaction = transaction;
    }
}
