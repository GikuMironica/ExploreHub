package feedbackComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import models.Account;
import models.Feedback;
import org.controlsfx.control.Rating;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Feedback and Rating controller
 * @author Diaae Bakri
 */

public class FeedbackController implements Initializable {
    /**
     * @FXML declarations
     */
    @FXML
    Rating rating;

    @FXML
    private TextArea description;

    @FXML
    private JFXButton sendFeedback;

    @FXML
    private Label thankYou;

    @FXML
    private Label feedbackOnce;

    private double Average;

    private EntityManager entityManager;

    private Account user = CurrentAccountSingleton.getInstance().getAccount();


    /**
     * Check if the user already posted a feedback before, if it's the case prevent him from rating
     * and sending a new feedback to the database.
     * Also calculating the average for all ratings is done here.
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

            entityManager = user.getConnection();
            int countUser = ((Number) entityManager.createNamedQuery("Feedback.checkUsers").
                    setParameter("UserID", user.getId()).getSingleResult()).intValue();

            if (countUser != 0) {
                TypedQuery<Feedback> userFeedback = entityManager.createNamedQuery("Feedback.getUserFeedback", Feedback.class);
                userFeedback.setParameter("UserID", user.getId());
                Feedback currentUserFeedback = userFeedback.getSingleResult();
                description.setText(currentUserFeedback.getRatingDescription());
                rating.setRating(currentUserFeedback.getRatingScore());
                description.setDisable(true);
                sendFeedback.setVisible(false);
                rating.setDisable(true);
                thankYou.setText("Thank you for your feedback");
                feedbackOnce.setText("You can only give a feedback once.");

            }
            // Calculate average
            Average = ((Number) entityManager.createNamedQuery("Feedback.getAverage").getSingleResult()).doubleValue();

    }



    /**
     * Submit the feedback to the database together with the rating score and the user who posted the feedback
     *
     * @param mouseEvent
     *
     */

    @FXML
    private void submitFeedback(MouseEvent mouseEvent) {

        Feedback feedback = new Feedback(rating.getRating(), description.getText(), user);

        try {
            entityManager = user.getConnection();
            entityManager.getTransaction().begin();
            entityManager.persist(feedback);
            entityManager.getTransaction().commit();
            description.setDisable(true);
            sendFeedback.setVisible(false);
            rating.setDisable(true);
            thankYou.setText("Thank you for your feedback");
            feedbackOnce.setText("You can only give a feedback once.");

            successAlert();



        } catch (PersistenceException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again");
        }
    }



    /**
     * Go back to the main UI
     *
     * @param mouseEvent
     */

    @FXML
    private void back(MouseEvent mouseEvent) {
        Convenience.closePreviousDialog();
    }




    /**
     * Average getter
     *
     * @return
     */
    public double getAverage() {
        return Average;
    }


    /**
     *
     */
    private void successAlert(){

       Convenience.showAlert(Alert.AlertType.INFORMATION,
               "Feedback",null, "Thank you for your feedback");
   }
}
