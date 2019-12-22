package authentification;

import handlers.Convenience;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main method
 * @author Gheorghe Mironica
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
        primaryStage.setTitle("ExploreHub");
        primaryStage.setResizable(false);

        // checks if remember me was ticked
        if(userDB.okState()) {
            userDB.setUser();
            GuestConnectionSingleton.getInstance().closeConnection();
            AuthentificationController.initiliaseApp();
            Convenience.switchScene(primaryStage, getClass().getResource("/FXML/mainUI.fxml"));

        }else {

            GuestConnectionSingleton.getInstance();
            StackPane mainRoot = new StackPane();
            BorderPane root = new BorderPane();
            mainRoot.getChildren().addAll(root);

            Convenience.switchScene(primaryStage, getClass().getResource("/FXML/authentification.fxml"));
        }
    }


    /**
     * Method closes the connection before exiting app
     */
    @Override
    public void stop(){

    }

    public static void main(String[] args) {
        launch(args);
    }

}
