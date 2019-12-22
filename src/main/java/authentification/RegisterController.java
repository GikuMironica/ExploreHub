package authentification;

import handlers.Convenience;
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
import mainUI.MainPane;
import models.Account;
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
@SuppressWarnings("JpaQueryApiInspection")
public class RegisterController implements Initializable  {
    @FXML
    private TextField firstNameField, lastNameField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ChoiceBox courseChoiceBox;
    private TypedQuery<Courses> tq1;
    private EntityManager entityManager;
    private String NAME_PATTERN = "^[a-zA-Z]*$";
    private String EMAIL_PATTERN = "[a-zA-Z0-9._]+@mail.hs-ulm\\.(de)$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManager = GuestConnectionSingleton.getInstance().getManager();

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
    private void register(Event e) throws IOException, InterruptedException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        Courses course = fetchCourse();

        if(isFormInvalid(firstName, lastName, email, password))
            return;

        // Everything Valid, persist new user
        try {
            User user = new User(firstName, lastName, email, password, course);
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            CurrentAccountSingleton currentUser = CurrentAccountSingleton.getInstance();
            currentUser.setAccount(user);

            GuestConnectionSingleton.getInstance().closeConnection();
            
        }catch(Exception ex){
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception ex1){}
        }

        AuthentificationController.initiliaseApp();


        Parent root = FXMLLoader.load(getClass().getResource("/FXML/mainUI.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    private Boolean isFormInvalid(String firstName, String lastName, String email, String password){
        TypedQuery<Account> tqa = entityManager.createNamedQuery("Account.findAccountByEmail", Account.class);
        tqa.setParameter("email",email);
        try{
            Account akk = tqa.getSingleResult();
            if (!(akk == null)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This user already exists");
                alert.showAndWait();
                return true;
            }
        }catch(Exception e){
            // account doesnt exit, continue
        }

        return validateFields(firstName, lastName, email, password);
    }

    /**
     * Method that validates the input text fields
     *
     * @param firstName firstname {@link String}
     * @param lastName last name {@link String}
     * @param email email {@link String}
     * @param password password {@link String}
     * @return true if all fields are valid otherwise false
     */
    private Boolean validateFields(String firstName, String lastName, String email, String password){
        // Validate Fields
        boolean ok = true;
        boolean validFirstName = (!(firstName.isEmpty())&&(firstName.matches(NAME_PATTERN)));
        boolean validLastName = (!(lastName.isEmpty())&&(lastName.matches(NAME_PATTERN)));
        boolean validEmail = (!(email.isEmpty())&&(email.matches(EMAIL_PATTERN)));
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
        return !ok;
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
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception e){}
            return new Courses();
        }
    }

    /**
     * Method which clears FirstName Label
     */
    @FXML
    private void fNameClick(){
        firstNameField.setText("");
        firstNameField.setStyle("-fx-text-inner-color: black;");

    }

    /**
     * Method which clears LastName Label
     */
    @FXML
    private void lNameClick(){
        lastNameField.setText("");
        lastNameField.setStyle("-fx-text-inner-color: black;");

    }

    /**
     * Method which clears Email Label
     */
    @FXML
    private void emailClick(){
        emailField.setText("");
        emailField.setStyle("-fx-text-inner-color: black;");

    }

    /**
     * Method which jumps back to previous scene
     *
     * @param event method trigger
     * @throws IOException {@link IOException}
     */
    @FXML
    private void goBack(Event event) throws IOException {
        Convenience.switchScene(event, getClass().getResource("/FXML/authentification.fxml"));
    }
}
