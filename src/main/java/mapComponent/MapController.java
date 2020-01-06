package mapComponent;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {
    @FXML
    private Button map;

    @FXML
    private ImageView mapView;

    @FXML
    private Label openInMapLabel;

    @FXML
    private Button mapButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void openMap(MouseEvent event) throws IOException {
        try {
            Desktop.getDesktop().browse(new URL("http://iexploremap.herokuapp.com/").toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies a shadow effect on the map image when it's being hovered over with a mouse.
     *
     * @param mouseEvent - the mouse enter event
     */
    @FXML
    private void handleMapEntered(MouseEvent mouseEvent) {
        animateShadowOnMap(0, -0.5, 1);
    }

    /**
     * Removes a shadow effect from the map image when it's not being hovered over with a mouse anymore.
     *
     * @param mouseEvent - the mouse exit event
     */
    @FXML
    private void handleMapExited(MouseEvent mouseEvent) {
        ColorAdjust colorAdjust = (ColorAdjust) mapView.getEffect();
        animateShadowOnMap(colorAdjust.getBrightness(), 0, 0);
    }

    /**
     * Animates a shadow effect on the map image within 400 ms.
     *
     * @param startingBrightness - initial brightness of the map image
     * @param targetBrightness - desired brightness of the map image
     * @param targetOpacity - desired opacity of the "Open in Map" label
     */
    private void animateShadowOnMap(double startingBrightness, double targetBrightness, double targetOpacity) {
        ColorAdjust colorAdjust = new ColorAdjust();
        mapView.setEffect(colorAdjust);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(colorAdjust.brightnessProperty(), startingBrightness),
                        new KeyValue(openInMapLabel.opacityProperty(), openInMapLabel.getOpacity())),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(colorAdjust.brightnessProperty(), targetBrightness),
                        new KeyValue(openInMapLabel.opacityProperty(), targetOpacity)));
        timeline.play();
    }
}
