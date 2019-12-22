package discussionComponent;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main{
    @FXML FontAwesomeIconView closeWindowButton;
    @FXML FontAwesomeIconView hideWindowButton;
    @FXML FontAwesomeIconView expandWindowButton;
    @FXML AnchorPane titleBar, contentPaneParent;

    private Stage discussionWindowStage;
    private Scene discussionWindowScene;
    private double windowMinimizedOffsetX;
    private double windowMinimizedOffsetY;
    private double yOffset = 0;
    private double xOffset = 0;
    private Boolean windowIsMaximized = false;

    public void initialize(Stage stage, Scene scene, Stage oldStage){
        Platform.runLater(()->{
            stage.show();
            stage.setScene(scene);
            oldStage.close();
            setStageAndScene(stage, scene);
        });

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titleBar.setOnMouseDragged(event -> {
            if (windowIsMaximized) expandWindow(event);
            discussionWindowStage.setX(event.getScreenX() - xOffset);
            discussionWindowStage.setY(event.getScreenY() - yOffset);
        });
        titleBar.setOnMouseClicked(event -> {
            if((event.getButton().equals(MouseButton.PRIMARY)) && (event.getClickCount() == 2)){
                expandWindow(event);
            }
        });

        try {
            AnchorPane contentPane = FXMLLoader.load(getClass().getResource("/FXML/discussion/contentPane.fxml"));
            contentPaneParent.getChildren().add(contentPane);
            System.out.println("This works");
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    @FXML void hideWindow(){
        Stage stage = (Stage) hideWindowButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML void expandWindow(MouseEvent event){
        Stage stage = discussionWindowStage;
        if(!windowIsMaximized){
            windowMinimizedOffsetX = stage.getX();
            windowMinimizedOffsetY = stage.getY();

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());

            windowIsMaximized = !windowIsMaximized;
            expandWindowButton.setGlyphName("COMPRESS");
        }else{
            stage.setWidth(1080.0);
            stage.setHeight(800.0);
            stage.setX(windowMinimizedOffsetX);
            stage.setY(windowMinimizedOffsetY);

            windowIsMaximized = !windowIsMaximized;
            expandWindowButton.setGlyphName("EXPAND");
        }
    }

    @FXML
    private void closeWindow(){
        Stage stage = (Stage) closeWindowButton.getScene().getWindow();
        stage.close();
    }

    private void setStageAndScene(Stage stage, Scene scene){
        this.discussionWindowStage = stage;
        this.discussionWindowScene = scene;
    }
}
