package VBoxForMapAndFilter;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CustomVBox extends VBox {
    public CustomVBox(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../mainUI/mainUI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
