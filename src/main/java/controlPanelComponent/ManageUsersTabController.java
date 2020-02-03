package controlPanelComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXTextArea;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import mainUI.MainPane;
import models.Account;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Public class that controls the manage users tab.
 *
 * @author Aleksejs Marmiss
 */
public class ManageUsersTabController implements Initializable {

    public AnchorPane anchorPane;
    @FXML
    private TextField studentLastName, studentName, studentEmail;
    @FXML
    private Button deleteButton, removeEventButton;
    @FXML
    private Label company;
    @FXML
    private Label companyName , date, city;
    @FXML
    private JFXTextArea description;
    @FXML
    private ImageView compImage;
    public ListView<User> listOfUsers;
    @FXML
    private ListView<Transactions> listOfBookings;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private ObservableList<User> users;
    private ObservableList<Transactions> transactions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOfBookings.setDisable(true);
    }

    /**
     * Method which is invoked when the cell is clicked. This method populates another listview by events booked bz the selected user.
     * @param event Event which was triggered by the click on list cell.
     */
    @FXML
    private void cellClicked(Event event){
        transactions = FXCollections.observableArrayList();
        User selectedUser = listOfUsers.getSelectionModel().getSelectedItem();
        studentName.setText(selectedUser.getFirstname());
        studentLastName.setText(selectedUser.getLastname());
        studentEmail.setText(selectedUser.getEmail());
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Transactions> usersEventsQuery;
        usersEventsQuery = entityManager.createNamedQuery(
               "Transactions.findTransactionsByUserId",
                   Transactions.class);
        usersEventsQuery.setParameter("id", selectedUser.getId());
        try {
            transactions.addAll(usersEventsQuery.getResultList());
        }catch (Exception e){
            if(!HandleNet.hasNetConnection()){
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(),anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
                    return;
                }catch(Exception exc){
                    Convenience.showAlert(CustomAlertType.WARNING, "Oops, something went wrong. Please, try again later.");
                }
            }else {
                Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
            }
        }
        listOfBookings.setItems(transactions);
        listOfBookings.setCellFactory(userBookingsCellController -> new UserBookingsCellController());
        company.setText("");
        city.setText("");
        description.setText("");
        if (transactions.size() != 0){
            listOfBookings.setDisable(false);
        }else {
            listOfBookings.setDisable(true);
        }
    }

    /**
     * Method which displazd the info about the particular event.
     * @param event Event which was triggered by the click on list cell.
     */
    @FXML
    private void cellCompanyClicked(Event event){
        Transactions selectedBooking = listOfBookings.getSelectionModel().getSelectedItem();
        if (!(selectedBooking == null)) {
            company.setText(selectedBooking.getEvent().getCompany());
            city.setText(selectedBooking.getEvent().getLocation().getCity());
            description.setText(selectedBooking.getEvent().getShortDescription());
        }
    }

    /**
     * Method that allows to delete a user from the database.
     * @param mouseEvent Event which was triggered by the click on button.
     */
    public void deleteUser(MouseEvent mouseEvent) {
        User selectedUser = listOfUsers.getSelectionModel().getSelectedItem();
        EntityManager entityManager = admin.getConnection();
        try{
            entityManager.getTransaction().begin();
            entityManager.remove(selectedUser);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            if(!HandleNet.hasNetConnection()){
                try {
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
                } catch (Exception exc) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                }
            }else {
                Convenience.showAlert(CustomAlertType.ERROR,
                        "Oops, looks like you have no internet connection. Try again later.");
            }
            entityManager.getTransaction().rollback();
        }

        User userToRemove = listOfUsers.getSelectionModel().getSelectedItem();
        users.remove(userToRemove);
    }

    /**
     * Method which allows to set a list of users to be used by the instance from the outside of the controller.
     * @param listOfUsers list of User objects.
     */
    public void setUsers(List<User> listOfUsers){
        users = FXCollections.observableArrayList();
        this.users.setAll(listOfUsers);
        this.listOfUsers.setItems(users);
        this.listOfUsers.setCellFactory(userCellController -> new UserCellController());
    }


    /**
     * Method which opens the homepage.
     * @param mouseEvent Mouse event triggered by the click of the button.
     */
    public void goHome(MouseEvent mouseEvent) {
        try{
            Convenience.openHome();
        }catch(Exception ex){
            Convenience.showAlert(CustomAlertType.WARNING, "Oops, something went wrong. Please, try again later.");
        }
    }
}
