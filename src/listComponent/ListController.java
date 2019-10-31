package listComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    @FXML
    private Button list;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list.setText("List here");
    }
}
