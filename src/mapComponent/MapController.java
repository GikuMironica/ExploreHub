package mapComponent;

import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {
    @FXML
    Button map;
    @FXML
    ImageView mapView;


    @FXML
    private Button mapButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void openMap(MouseEvent event) throws IOException {
        try {
            Desktop.getDesktop().browse(new URL("http://iexploremap.herokuapp.com/").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
