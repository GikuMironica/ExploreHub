package mainUI;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import listComponent.ListController;
import mapComponent.MapController;
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

