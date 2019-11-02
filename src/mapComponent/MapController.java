package mapComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {
    @FXML
    Button map;

    @FXML
    private Button mapButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void registerClick(){
        System.out.println("Map button clicked");
    }
}
