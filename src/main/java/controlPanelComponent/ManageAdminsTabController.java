package controlPanelComponent;


import authentification.CurrentAccountSingleton;
import handlers.MessageHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import handlers.Convenience;
import handlers.UploadImage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.Account;
import models.Admin;
import models.Courses;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.File;
import java.util.Optional;
import java.util.UUID;

public class ManageAdminsTabController{

    @FXML
    private JFXTextField firstnameText, lastnameText, emailText;
    @FXML
    private ImageView adminPicture;
    @FXML
    private String imageURL;
    @FXML
    private JFXButton uploadButton, deleteAdminButton;
    @FXML
    private JFXListView<Admin> adminListView;
    private ObservableList<Admin> adminObservableList;
    private EntityManager entityManager;
    private Account account;
    private String NAME_PATTERN = "^[a-zA-Z]*$";
    private String EMAIL_PATTERN = "[a-zA-Z0-9._]+@mail.hs-ulm\\.(de)$";
    private String ADMIN_CREATED_MESSAGE = "Admin account was successfully created";
    private String PASSWORD_GENERATED = "Random password was generated, sent over email";
    private Admin selectedAdmin;

    @SuppressWarnings("JpaQueryApiInspection")
    public void initialize() {
        try{
            imageURL = null;
            account = CurrentAccountSingleton.getInstance().getAccount();
            entityManager = account.getConnection();
            if(selectedAdmin == null){
                deleteAdminButton.setDisable(true);
            }
            TypedQuery<Admin> tq1 = entityManager.createNamedQuery("Admin.findAdmins", Admin.class);
            adminObservableList = FXCollections.observableArrayList();
            adminObservableList.addAll(tq1.getResultList());
            int index = adminObservableList.indexOf(account);
            adminObservableList.remove(index);
            adminListView.setItems(adminObservableList);
            adminListView.setCellFactory(customListViewCell -> new AdminCellController());

        }catch(Exception e){
            e.printStackTrace();

        }

    }

    @FXML
    public void createAdmin(Event e){
        String firstname = firstnameText.getText();
        String lastname = lastnameText.getText();
        String email = emailText.getText();
        String rand = generateString();

        try{
            if(isFormInvalid(firstname, lastname, email)){
                return;
            }
            Admin newAdmin = new Admin(firstname, lastname, email, 1, rand, fetchCourse(), imageURL);

            entityManager.getTransaction().begin();
            entityManager.persist(newAdmin);
            entityManager.getTransaction().commit();

            clearView(e);
            adminObservableList.add(newAdmin);
            Convenience.showAlert(Alert.AlertType.INFORMATION,"Admin Created", ADMIN_CREATED_MESSAGE, PASSWORD_GENERATED);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        try{
            MessageHandler.getMessageHandler().sendNewPassword(rand, email);
        } catch(Exception ex){
            // unexistent email
        }
    }


    @FXML
    public void deleteAdmin(Event e){
        try{
            if (selectedAdmin != null) {
                Optional<ButtonType> response = Convenience.showAlertWithResponse(Alert.AlertType.CONFIRMATION, "Delete Admin", "Are you sure you want to remove: ",
                        selectedAdmin.getFirstname()+" "+selectedAdmin.getLastname(), ButtonType.YES, ButtonType.CANCEL);

                if (response.isPresent() && response.get() == ButtonType.CANCEL) {
                    return;
                } else {
                    entityManager.getTransaction().begin();
                    entityManager.remove(selectedAdmin);
                    entityManager.getTransaction().commit();
                    adminObservableList.remove(selectedAdmin);
                    clearView(e);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void uploadPicture(Event e){
        Image image;
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG Files", "*.png"));
            File file = fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());

            if(file!=null){
                uploadButton.setText("Image Loaded");
                uploadButton.setStyle("-fx-text-fill: green;");
                image = new Image(file.toURI().toString());
                adminPicture.setImage(image);
                UploadImage uploader = new UploadImage(image);
                imageURL = uploader.upload();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void cellClicked(Event event){
        try {
            deleteAdminButton.setDisable(false);
            selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
            firstnameText.setText(selectedAdmin.getFirstname());
            lastnameText.setText(selectedAdmin.getLastname());
            emailText.setText(selectedAdmin.getEmail());
            adminPicture.setImage(new Image(selectedAdmin.getPicture()));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void clearView(Event e){
        firstnameText.clear();
        lastnameText.clear();
        emailText.clear();
        Image tempImage = new Image("/IMG/icon-account.png");
        adminPicture.setImage(tempImage);
    }

    private Courses fetchCourse() {
        try{

            @SuppressWarnings("JpaQueryApiInspection")
            TypedQuery<Courses> tq1 = entityManager.createNamedQuery(
                    "Courses.findCourseByName",
                    Courses.class);
            tq1.setParameter("name", "None");
            return tq1.getSingleResult();
        }catch(Exception er){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Check the internet connection...");
            alert.showAndWait();
            return new Courses();
        }
    }

    private boolean isFormInvalid(String firstname, String lastname, String email) {
        return !areFieldsValid(firstname, lastname, email);
    }

    private boolean areFieldsValid(String firstname, String lastname, String email) {
        boolean valid = true;
        boolean validFirstName = (!(firstname.isEmpty())&&(firstname.matches(NAME_PATTERN)));
        boolean validLastName = (!(lastname.isEmpty())&&(lastname.matches(NAME_PATTERN)));
        boolean validEmail = (!(email.isEmpty())&&(email.matches(EMAIL_PATTERN)));

        if(!validFirstName){
            firstnameText.setStyle("-fx-text-inner-color: red;");
            firstnameText.setText("Invalid First Name");
            displayError(firstnameText);
            valid = false;
        }
        if(!validLastName){
            lastnameText.setStyle("-fx-text-inner-color: red;");
            lastnameText.setText("Invalid Last Name");
            displayError(lastnameText);
            valid = false;
        }
        if(!validEmail){
            emailText.setStyle("-fx-text-inner-color: red;");
            emailText.setText("Invalid email");
            displayError(emailText);
            valid = false;
        }

        return valid;
    }

    private void displayError(JFXTextField field) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (Exception e) { }
            Platform.runLater(() -> {
                field.clear();
                field.setStyle("-fx-text-inner-color: black;");
            });
        });
        thread.start();
    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return "uuid = " + uuid;
    }

    @FXML
    public void goHome(Event e){
        try{
            Convenience.switchScene(e, getClass().getResource("/FXML/mainUI.fxml"));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
