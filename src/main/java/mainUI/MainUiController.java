package mainUI;

import authentification.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import models.Account;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("JpaQueryApiInspection")
public class MainUiController implements Initializable {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainPane.getInstance().setStackPane(mainStackPane);
        MainPane.getInstance().setBorderPane(mainBorderPane);
        setLogin();
    }

    private void setLogin() {
        Account akk = CurrentAccountSingleton.getInstance().getAccount();
        EntityManager entityManager = akk.getConnection();
        int id = akk.getId();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("UPDATE users SET users.Active = 1 WHERE users.Id = ?").setParameter(1, id).executeUpdate();
        entityManager.getTransaction().commit();

        long active = 0;
        Query q1 = entityManager.createNamedQuery("Account.findNrActive", Account.class);
        active = (long)q1.getSingleResult();
    }

    /**
     * This method is called whenever the user clicks anywhere on the main UI.
     * The main UI will get the focus if this event occurs, making other UI elements
     * lose their focus.
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleMainUiPressed(MouseEvent mouseEvent) {
        mainBorderPane.requestFocus();
    }
}

