package feedbackComponent;

import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import models.Account;
import models.Feedback;
import org.controlsfx.control.Rating;

import javax.persistence.EntityManager;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Feedback and Rating controller
 * @author Diaae Bakri
 */

public class FeedbackController implements Initializable {
    /**
     * @FXML declarations
     */
    @FXML Rating rating;

    @FXML private TextArea description;

    private double average;


    private Account user = CurrentAccountSingleton.getInstance().getAccount();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /**
     * Submit the feedback to the database together with the rating score and the user who posted the feedback
     * @param mouseEvent
     * @throws SQLException
     */

    @FXML
    private void submitFeedback(MouseEvent mouseEvent) throws SQLException {
        Feedback feedback = new Feedback(rating.getRating(), description.getText(), user);
        EntityManager entityManager = user.getConnection();
        entityManager.getTransaction().begin();
        entityManager.persist(feedback);
        entityManager.getTransaction().commit();
    }


    /**
     * Go back to the main UI
     * @param mouseEvent
     */

    @FXML
    private void back(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Average getters and setters
     * @return
     */
    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }
}
