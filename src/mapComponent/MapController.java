package mapComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {
    @FXML
    Button map;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map.setText("Map here");
    }
}
