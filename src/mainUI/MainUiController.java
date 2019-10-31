package mainUI;

import authentification.AuthentificationController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainUiController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BorderPane root = new BorderPane();
        FXMLLoader loaderCenter = new FXMLLoader(getClass().getResource("/listComponent/list.fxml"));
        FXMLLoader loaderTop = new FXMLLoader(getClass().getResource("/barComponent/navbar.fxml"));
        FXMLLoader loaderLeftTop = new FXMLLoader(getClass().getResource("/filterComponent/filter.fxml"));
        FXMLLoader loaderLeftBottom = new FXMLLoader(getClass().getResource("/mapComponent/map.fxml"));
        FXMLLoader loaderRight = new FXMLLoader(getClass().getResource("/sidebarComponent/sidebar.fxml"));

        VBox leftBox = new VBox();
        leftBox.getChildren().add(loaderLeftTop.getRoot());
        leftBox.getChildren().add(loaderLeftBottom.getRoot());

        try {
            root.setCenter(loaderCenter.load());
            root.setRight(loaderRight.load());
            root.setLeft(leftBox);
            root.setTop(loaderTop.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

