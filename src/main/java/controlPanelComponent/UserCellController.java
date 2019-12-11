package controlPanelComponent;


import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.Account;
import models.User;

import javax.persistence.EntityManager;

public class UserCellController extends ListCell<User> {

    private FXMLLoader loader;
    private EntityManager entityManager;
    @FXML
    private Label name, surname, email;
    @FXML
    private ImageView userImage;
    @FXML
    public HBox userCell;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();


    @Override
    public void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/FXML/userCell.fxml"));
                        loader.setController(this);
                    }

                    try {
                        loader.load();
                        entityManager = admin.getConnection();

                    } catch (Exception e) {
                     //   e.printStackTrace();
                     //   Platform.runLater(() -> Convenience.showAlert(Alert.AlertType.ERROR, "Ooops", "Something went wrong", "Please try again later"));

                    }
                    name.setText(user.getFirstname());
                    surname.setText(user.getLastname());
                    email.setText(user.getEmail());
                    userImage.setImage(new Image("/IMG/icon-account.png"));

                    setText(null);
                    setGraphic(userCell);

                }


            }
}