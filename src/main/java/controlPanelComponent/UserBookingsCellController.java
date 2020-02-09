package controlPanelComponent;


import authentification.loginProcess.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.Account;
import models.Transactions;


import javax.persistence.EntityManager;

public class UserBookingsCellController extends ListCell<Transactions> {

    private FXMLLoader loader;
    private EntityManager entityManager;
    @FXML
    public HBox bookingCell;
    @FXML
    private Label companyName , date, city;
    @FXML
    private ImageView compImage;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();


    @Override
    public void updateItem(Transactions transaction, boolean empty) {
        super.updateItem(transaction, empty);
                if (empty || transaction == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/FXML/bookingCell.fxml"));
                        loader.setController(this);
                    }

                    try {
                        loader.load();
                        entityManager = admin.getConnection();

                    } catch (Exception e) { //e.printStackTrace();
                     //  Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.ERROR, "Ooops", "Something went wrong", "Please try again later"));

                    }
                    companyName.setText(transaction.getEvent().getCompany());
                    date.setText(transaction.getEvent().getDate().toString());
                    city.setText(transaction.getEvent().getLocation().getCity());
                    compImage.setImage(new Image(transaction.getEvent().getPicture().getLogo()));

                    setText(null);
                    setGraphic(bookingCell);

                }


            }
}