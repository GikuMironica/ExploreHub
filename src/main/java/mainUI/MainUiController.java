package mainUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainUiController implements Initializable {

    @FXML
    private StackPane mainStackPane;

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainStackPane.getInstance().setStackPane(mainStackPane);
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

    public BorderPane getMainBorderPane() {
        return mainBorderPane;
    }
}

