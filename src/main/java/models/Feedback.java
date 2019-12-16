package models;

import models.Account;
import models.User;

import javax.persistence.*;

@SuppressWarnings("JpaQlInspection")
@NamedQuery(name="Feedback.findAllFeedbacks", query="SELECT f FROM Feedback f")

@Entity
@Table(name = "feedback")

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
    @JoinColumn(name="UserID")
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

    public Account getUserID() {
        return UserID;
    }

    public void setUserID(Account userID) {
        UserID = userID;
    }
}
