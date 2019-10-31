package filterComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.lang.management.BufferPoolMXBean;
import java.net.URL;
import java.util.ResourceBundle;

public class FilterController implements Initializable {
    @FXML
    private Button filter;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filter.setText("Filter here");
    }
}
