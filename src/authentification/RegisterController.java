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

public class RegisterController implements Initializable  {

    @FXML
    private TextField firstNameField, lastNameField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox courseChoiceBox;
    private TypedQuery<Courses> tq1;


    UserConnectionSingleton con = UserConnectionSingleton.getInstance();
    EntityManager entityManager = con.getManager();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
            // Views setup
        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        emailField.setPromptText("emailField");
        passwordField.setPromptText("New passwordField");


            // get courses from DB -> display into ChoiseBox
        tq1 = entityManager.createNamedQuery(
                "Courses.findCourses",
                Courses.class);
        List<Courses> l1 = tq1.getResultList();
        for( Courses temp : l1){
            courseChoiceBox.getItems().add(temp.getName());
        }

        courseChoiceBox.getSelectionModel().selectFirst();

    }

    public void register(Event e) throws IOException {
        boolean fieldsInvalid = false;
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        Courses course = new Courses();

        // get Course ID by Name, check connection
        try{
            String courseName = courseChoiceBox.getSelectionModel().getSelectedItem().toString();
            tq1 = entityManager.createNamedQuery(
                "Courses.findCourseByName",
                Courses.class);
            tq1.setParameter("name", courseName);
            course = tq1.getSingleResult();
        }catch(Exception er){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
        }

            // Validate Fields
        boolean validFirstName = (!(firstName.isEmpty())&&(firstName.matches("^[a-zA-Z]*$")));
        boolean validLastName = (!(lastName.isEmpty())&&(lastName.matches("^[a-zA-Z]*$")));
        boolean validEmail = (!(email.isEmpty())&&(email.matches("[a-zA-Z0-9._]+@hs-ulm\\.(de)$")));
        boolean validPassword = (!(password.isEmpty()));

        if(!validFirstName){
            firstNameField.setText("Input valid name");
            firstNameField.setStyle("-fx-text-inner-color: red;");
            fieldsInvalid = true;
        }
        if(!validLastName){
            lastNameField.setText("Input valid name");
            lastNameField.setStyle("-fx-text-inner-color: red;");
            fieldsInvalid = true;
        }
        if(!validEmail){
            emailField.setText("Input a valid HS emailField");
            emailField.setStyle("-fx-text-inner-color: red;");
            fieldsInvalid = true;
        }
        if(!validPassword){
            passwordField.setPromptText("Input a password");
            fieldsInvalid = true;
        }
        if(fieldsInvalid)
            return;

            // Everything valid a this step
            // Persist new user
        try {
            User u1 = new User(firstName, lastName, email, password, course);
            entityManager.getTransaction().begin();
            entityManager.persist(u1);
            entityManager.getTransaction().commit();
            CurrentUserSingleton currentUser = CurrentUserSingleton.getInstance();
            currentUser.setUser(u1);
        }catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
        }

        Parent root = FXMLLoader.load(getClass().getResource("../mainUI/mainUi.fxml"));
        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add("/mainUI/style.css");
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    public void fNameClick(){
        firstNameField.setText("");
        firstNameField.setStyle("-fx-text-inner-color: black;");

    }
    public void lNameClick(){
        lastNameField.setText("");
        lastNameField.setStyle("-fx-text-inner-color: black;");

    }
    public void emailClick(){
        emailField.setText("");
        emailField.setStyle("-fx-text-inner-color: black;");

    }
}
