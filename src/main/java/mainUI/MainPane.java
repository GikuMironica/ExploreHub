package mainUI;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * A singleton class that keeps the reference to the root StackPane of the main UI.
 * @author Aleksejs Marmiss
 */
public class MainPane {

    private static MainPane instance;

    private StackPane stackPane;
    private BorderPane borderPane;

    private MainPane() {}

    public static MainPane getInstance() {
        if (instance == null) {
            instance = new MainPane();
        }
        return instance;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }
}
