package mainUI;

import authentification.AuthentificationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import listComponent.ListController;
import mapComponent.MapController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainUiController implements Initializable {

    @FXML
    private MapController mapController;
    @FXML
    private ListController listController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}

