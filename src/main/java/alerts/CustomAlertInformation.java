package alerts;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class that represents the custom JFoenix information alert.
 *
 * @author Hidayat Rzayev
 */
public class CustomAlertInformation extends CustomAlert {

    /**
     * Sets the heading of the alert to contain the information icon and text.
     */
    @Override
    public void setHeading() {
        Image informationImage = new Image("/IMG/info.png");
        ImageView headingImageView = new ImageView(informationImage);
        Label headingText = this.getAlertMessage("Information", HEADING_MESSAGE_FONT, HEADING_FONT_SIZE);
        this.heading.getChildren().addAll(headingImageView, headingText);
    }
}
