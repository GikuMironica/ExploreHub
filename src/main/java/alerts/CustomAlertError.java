package alerts;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that represents the custom JFoenix error alert.
 *
 * @author Hidayat Rzayev
 */
public class CustomAlertError extends CustomAlert {

    /**
     * Sets the heading of the alert to contain the error icon and text.
     */
    @Override
    public void setHeading() {
        Image errorImage = new Image("/IMG/error.png");
        ImageView headingImageView = new ImageView(errorImage);
        Label headingText = this.getAlertMessage("Error", HEADING_MESSAGE_FONT, HEADING_FONT_SIZE);
        this.heading.getChildren().addAll(headingImageView, headingText);
    }
}
