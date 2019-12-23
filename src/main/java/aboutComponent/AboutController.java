package aboutComponent;
/**
 * @author Abdul Basit
 */

import handlers.Convenience;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    /**
     * Returns back to the homepage
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleBackFromAboutClicked(MouseEvent mouseEvent) {
        Convenience.closePreviousDialog();
    }
}
