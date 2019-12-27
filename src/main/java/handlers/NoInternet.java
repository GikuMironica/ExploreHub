package handlers;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXSpinner;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import models.Events;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class which handles loss of internet connection,
 * displays a Pop Up with the respective message,
 * polls the server in a loop until connection is reestablished
 * and locks the UI in the meanwhile
 *
 * @author Gheorghe Mironica
 */
public class NoInternet implements Initializable {

    private FXMLLoader loader;
    @FXML
    private AnchorPane noInternetAnchorPane;
    @FXML
    private JFXSpinner spinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pollServer();
    }

    /**
     * Method which polls the server in the loop until connection is reestablished
     */
    private synchronized void pollServer(){
        Thread loop = new Thread(()-> {
            while(!HandleNet.hasNetConnection()){
                Convenience.getDialog().setOverlayClose(false);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {e.printStackTrace(); }
            }
            Convenience.getDialog().setOverlayClose(true);
            Convenience.closePreviousDialog();
        });
        if(!loop.isAlive())
             loop.start();
    }
}
