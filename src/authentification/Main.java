package authentification;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import listComponent.EventListSingleton;

/**
 * Main method
 * @author Gheorghe Mironica
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        FXMLLoader atLoader = new FXMLLoader(getClass().getResource("/authentification/authentification.fxml"));
        root.setCenter(atLoader.load());
        AuthentificationController atController = atLoader.getController();
        atController.init();

        //Initialize listView in a separate Thread
        Thread thread = new Thread(){
            public void run(){
                EventListSingleton ev = EventListSingleton.getInstance();
            }
        };
        thread.start();

        System.out.println("Login scene is loading");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
