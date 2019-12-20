package models;

import javax.persistence.*;

/**
 * Model class which encapsulates access to feedbacks
 * @author Diaae Bakri
 */

@Entity
@Table(name = "feedback")
@NamedQueries({

        @NamedQuery(name = "Feedback.findAllFeedbacks", query = "SELECT feedB from Feedback feedB"),
        @NamedQuery(name = "Feedback.checkUsers", query = "SELECT COUNT (u) FROM Feedback u WHERE u.UserID.Id = :UserID"),
        @NamedQuery(name = "Feedback.getUserFeedback", query = "SELECT f FROM Feedback f WHERE f.UserID.Id = :UserID"),
        @NamedQuery(name = "Feedback.getAverage", query = "SELECT AVG (u.ratingScore) FROM Feedback u")

})

public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer feedbackID;
    @Column(name = "Rating", nullable = false)
    private Double ratingScore;
    @Column(name = "Message", length = 250)
    private String ratingDescription;
    /*
     * Mapping goes here
     */
    @OneToOne
    @JoinColumn(name="UserID", unique = true)
    private Account UserID;


    /*
     * Constructor goes here
     */

    public Feedback(Double ratingScore, String ratingDescription, Account userID) {
        this.ratingScore = ratingScore;
        this.ratingDescription = ratingDescription;
        UserID = userID;
    }

    public Feedback() {
    }

    /*
     * Getters and Setters
     */

    public Integer getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(Integer feedbackID) {
        this.feedbackID = feedbackID;
    }

    public Double getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(Double ratingScore) {
        this.ratingScore = ratingScore;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public Account getUserID() { return UserID; }

    public void setUserID(Account userID) { UserID = userID; }
}
