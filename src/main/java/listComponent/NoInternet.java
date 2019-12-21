package listComponent;

import com.jfoenix.controls.JFXListCell;
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

public class NoInternet implements Initializable {

    private FXMLLoader loader;
    @FXML
    private AnchorPane noInternetAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

