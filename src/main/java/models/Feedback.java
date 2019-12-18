package models;

import javax.persistence.*;

/**
 * Model class which encapsulates the data of the Feedback entity and the logic to manage it
 *
 * @author Gheorghe Mironica
 */
@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Feedback.findAllFeedbacks", query="SELECT f FROM Feedback f")
})

@Entity
@Table(name="feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @OneToOne
    @JoinColumn(name="UserID")
    private Account account;

    @Basic(optional = false)
    private int Rating;

    @Basic(optional = false)
    private String Message;

    /**
     * default constructor
     */
    public Feedback(){
        // ctor
    }

    /**
     * Overloaded costructor
     *
     * @param akk The user who posted the feedback {@link Account}
     * @param rating The given rating 1-5 {@link Integer}
     * @param message The message {@link String}
     */
    public Feedback(Account akk, int rating, String message){
        this.account = akk;
        this.Rating = rating;
        this.Message = message;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
