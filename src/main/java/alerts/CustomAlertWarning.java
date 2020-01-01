package alerts;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that represents the custom JFoenix warning alert.
 *
 * @author Hidayat Rzayev
 */
public class CustomAlertWarning extends CustomAlert {

    /**
     * Sets the heading of the alert to contain the warning icon and text.
     */
    @Override
    public void setHeading() {
        Image warningImage = new Image("/IMG/warning.png");
        ImageView headingImageView = new ImageView(warningImage);
        Label headingText = this.getAlertMessage("Warning", HEADING_MESSAGE_FONT, HEADING_FONT_SIZE);
        this.heading.getChildren().addAll(headingImageView, headingText);
    }
}
