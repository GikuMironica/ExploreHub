package authentification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import listComponent.EventListSingleton;
import listComponent.UpdateListTask;

import java.util.Timer;

/**
 * Main method
 * @author Gheorghe Mironica
 */
public class Main extends Application {

    private Timer timer;
    private UpdateListTask updateTask;
    private long delay = 0;
    private long interval = 15000;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        FXMLLoader atLoader = new FXMLLoader(getClass().getResource("/authentification/authentification.fxml"));
        root.setCenter(atLoader.load());
        AuthentificationController atController = atLoader.getController();
        atController.init();

        //Initialize listView in a separate Thread
        Thread thread = new Thread(() -> {
            EventListSingleton ev = EventListSingleton.getInstance();
        });
        thread.start();


        // Start Job to update regularly the ListView in background
        timer = new Timer();
        updateTask = new UpdateListTask();
        timer.scheduleAtFixedRate(updateTask, delay, interval);
        // System.out.println("Login scene is loading");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Method which stops the Scheduled Background Job before exiting app
     */
    @Override
    public void stop(){
        System.out.println("Stage is closing");
        timer.cancel();
        timer.purge();
        try {
            CurrentAccountSingleton.getInstance().getAccount().getConnection().close();
        }catch(Exception e){
            UserConnectionSingleton.getInstance().getManager().close();
            AdminConnectionSingleton.getInstance().getManager().close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
