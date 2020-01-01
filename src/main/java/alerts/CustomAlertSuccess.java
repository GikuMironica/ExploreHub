package alerts;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that represents the custom JFoenix success alert.
 *
 * @author Hidayat Rzayev
 */
public class CustomAlertSuccess extends CustomAlert {

    /**
     * Sets the heading of the alert to contain the success icon and text.
     */
    @Override
    public void setHeading() {
        Image confirmationImage = new Image("/IMG/success.png");
        ImageView headingImageView = new ImageView(confirmationImage);
        Label headingText = this.getAlertMessage("Success", HEADING_MESSAGE_FONT, HEADING_FONT_SIZE);
        this.heading.getChildren().addAll(headingImageView, headingText);
    }
}
