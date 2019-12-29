package authentification;

import alerts.CustomAlertType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import handlers.Convenience;
import handlers.MessageHandler;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    private StackPane registerStackPane;
    @FXML
    private AnchorPane registerAnchorPane;
    @FXML
    private JFXTextField firstNameField, lastNameField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private JFXComboBox courseChoiceBox;
    private TypedQuery<Courses> tq1;
    private EntityManager entityManager;
    private String NAME_PATTERN = "^[a-zA-Z]*$";
    private String EMAIL_PATTERN = "[a-zA-Z0-9._]+@mail.hs-ulm\\.(de)$";
    private final String EMAIL_SUBBJECT = "Registration Confirmation";
    private final String EMAIL_LETTER = "Your have successfully registered to Explore Hub";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManager = GuestConnectionSingleton.getInstance().getManager();

        /*firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        emailField.setPromptText("emailField");
        passwordField.setPromptText("New passwordField");*/

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
     * @throws IOException thrown exception
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
        if(isFormInvalid(firstName, lastName, email, password))
            return;

        // Everything Valid, persist new user
        try {
            User user = new User(firstName, lastName, email, password, course, "/IMG/icon-account.png");
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            CurrentAccountSingleton currentUser = CurrentAccountSingleton.getInstance();
            currentUser.setAccount(user);

        }catch(Exception ex){
            try {
                Convenience.popupDialog(registerStackPane, registerAnchorPane, getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception ex1){/**/}
        }
        try{
            MessageHandler messageHandler = MessageHandler.getMessageHandler();
            String Email = CurrentAccountSingleton.getInstance().getAccount().getEmail();
            messageHandler.sendEmail(EMAIL_LETTER,EMAIL_SUBBJECT,Email);
        }catch(Exception exc){
            // unexistent email
        }

        //Convenience.popupDialog(registerStackPane, registerAnchorPane, getClass().getResource("/FXML/successRegister.fxml"));
        Convenience.showAlert(CustomAlertType.SUCCESS,"You have successfully registered for ExploreHub! Welcome on board!");
        Convenience.switchScene(e, getClass().getResource("/FXML/authentification.fxml"));
    }

    /**
     * Method which validates the form
     * @param firstName {@link String} first name
     * @param lastName {@link String} last name
     * @param email {@link String} email
     * @param password {@link String} password
     * @return {@link Boolean} true / false
     */
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
                Convenience.popupDialog(registerStackPane, registerAnchorPane, getClass().getResource("/FXML/noInternet.fxml"));
            }catch(Exception e){/**/}
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
