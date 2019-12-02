package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Account;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ManageUsersTabController implements Initializable {

    @FXML
    private TextField studentLastName, studentName, studentEmail;
    @FXML
    private Button deleteButton, removeEventButton;
    @FXML
    private Label company;
    @FXML
    private Label companyName , date, city, description;
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

    }


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
               "Transactions.findTransactionsById",
                   Transactions.class);
        usersEventsQuery.setParameter("id", selectedUser.getId());
        transactions.addAll(usersEventsQuery.getResultList());
        listOfBookings.setItems(transactions);
        listOfBookings.setCellFactory(userBookingsCellController -> new UserBookingsCellController());
        company.setText("");
        city.setText("");
        description.setText("");
    }

    @FXML
    private void cellCompanyClicked(Event event){
        Transactions selectedBooking = listOfBookings.getSelectionModel().getSelectedItem();
        company.setText(selectedBooking.getEvent().getCompany());
        city.setText(selectedBooking.getEvent().getLocation().getCity());
        description.setText(selectedBooking.getEvent().getShortDescription());

    }


    public void deleteUser(MouseEvent mouseEvent) {
        User selectedUser = listOfUsers.getSelectionModel().getSelectedItem();
        EntityManager entityManager = admin.getConnection();
        entityManager.getTransaction().begin();
        entityManager.remove(selectedUser);
        entityManager.getTransaction().commit();
        User userToRemove = listOfUsers.getSelectionModel().getSelectedItem();
        users.remove(userToRemove);
    }

    public void setUsers(List<User> listOfUsers){
        users = FXCollections.observableArrayList();
        this.users.setAll(listOfUsers);
        this.listOfUsers.setItems(users);
        this.listOfUsers.setCellFactory(userCellController -> new UserCellController());
    }
}
