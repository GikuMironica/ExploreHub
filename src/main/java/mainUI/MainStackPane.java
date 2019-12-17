package mainUI;

import javafx.scene.layout.StackPane;

/**
 * A singleton class that keeps the reference to the root StackPane of the main UI.
 * @author Aleksejs Marmiss
 */
public class MainStackPane {

    private static MainStackPane instance;
    private StackPane stackPane;

    private MainStackPane() {}

    public static MainStackPane getInstance() {
        if (instance == null) {
            instance = new MainStackPane();
        }
        return instance;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }
}
