package mapComponent;

import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
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
    public void registerClick(Event event) throws IOException {
        Convenience.switchScene(event, getClass().getResource("/mapComponent/mapview.fxml"));
    }
}
