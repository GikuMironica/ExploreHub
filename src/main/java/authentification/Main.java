package authentification;

import handlers.Convenience;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import listComponent.EventListSingleton;
import listComponent.UpdateListTask;

import java.util.Timer;

/**
 * Main method
 * @author Gheorghe Mironica
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();

        // checks if remember me was ticked
        if(userDB.okState()) {
            userDB.setUser();
            Convenience.switchScene(primaryStage, getClass().getResource("/FXML/mainUI.fxml"));

        }else {

            StackPane mainRoot = new StackPane();
            BorderPane root = new BorderPane();
            FXMLLoader atLoader = new FXMLLoader(getClass().getResource("/FXML/authentification.fxml"));
            root.setCenter(atLoader.load());
            AuthentificationController atController = atLoader.getController();
            atController.init();
            mainRoot.getChildren().addAll(root);

            // System.out.println("Login scene is loading");
            Scene scene = new Scene(mainRoot, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ExploreHub");
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }


    /**
     * Method closes the connection before exiting app
     */
    @Override
    public void stop(){
        try {
            AuthentificationController.stop();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
