package authentification;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Courses;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *Class that handles the registration process
 * @author Gheorghe Mironica
 */
public class RegisterController implements Initializable  {
    @FXML
    private TextField firstNameField, lastNameField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox courseChoiceBox;
    private TypedQuery<Courses> tq1;
    private UserConnectionSingleton con = UserConnectionSingleton.getInstance();
    private EntityManager entityManager = con.getManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        emailField.setPromptText("emailField");
        passwordField.setPromptText("New passwordField");

        //noinspection JpaQueryApiInspection
        tq1 = entityManager.createNamedQuery(
                "Courses.findCourses",
                Courses.class);
        List<Courses> l1 = tq1.getResultList();
        for( Courses temp : l1){
            courseChoiceBox.getItems().add(temp.getName());
        }
        courseChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Method that handles the registration process
     * @param e pressed button triggers event
     * @throws IOException
     */
    @FXML
    private void register(Event e) throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        Courses course = fetchCourse();

        if(!validateFields(firstName, lastName, email, password))
            return;

        // Everything Valid, persist new user
        try {
            User user = new User(firstName, lastName, email, password, course);
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            CurrentAccountSingleton currentUser = CurrentAccountSingleton.getInstance();
            currentUser.setAccount(user);
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/mainUI/mainUi.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    /**
     * Method that validates the input text fields
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return true if all fields are valid otherwise false
     */
    private Boolean validateFields(String firstName, String lastName, String email, String password){
        // Validate Fields
        boolean ok = true;
        boolean validFirstName = (!(firstName.isEmpty())&&(firstName.matches("^[a-zA-Z]*$")));
        boolean validLastName = (!(lastName.isEmpty())&&(lastName.matches("^[a-zA-Z]*$")));
        boolean validEmail = (!(email.isEmpty())&&(email.matches("[a-zA-Z0-9._]+@mail.hs-ulm\\.(de)$")));
        boolean validPassword = (!(password.isEmpty()));

        if(!validFirstName){
            firstNameField.setText("Input valid name");
            firstNameField.setStyle("-fx-text-inner-color: red;");
            ok = false;
        }
        if(!validLastName){
            lastNameField.setText("Input valid name");
            lastNameField.setStyle("-fx-text-inner-color: red;");
            ok = false;
        }
        if(!validEmail){
            emailField.setText("Input a valid HS emailField");
            emailField.setStyle("-fx-text-inner-color: red;");
            ok = false;
        }
        if(!validPassword){
            passwordField.setPromptText("Input a password");
            ok = false;
        }
        return ok;
    }

    /**
     * Method which gets the selected Course from ChoiseBox and return a Course Object
     *
     * @return {@link Courses} object.
     */
    private Courses fetchCourse() {
        try{
            String courseName = courseChoiceBox.getSelectionModel().getSelectedItem().toString();
            //noinspection JpaQueryApiInspection
            tq1 = entityManager.createNamedQuery(
                    "Courses.findCourseByName",
                    Courses.class);
            tq1.setParameter("name", courseName);
            return tq1.getSingleResult();
        }catch(Exception er){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            return new Courses();
        }
    }
    @FXML
    private void fNameClick(){
        firstNameField.setText("");
        firstNameField.setStyle("-fx-text-inner-color: black;");

    }
    @FXML
    private void lNameClick(){
        lastNameField.setText("");
        lastNameField.setStyle("-fx-text-inner-color: black;");

    }
    @FXML
    private void emailClick(){
        emailField.setText("");
        emailField.setStyle("-fx-text-inner-color: black;");

    }
}
