package alerts;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.Optional;
import javafx.scene.control.ButtonType;

/**
 * Abstract class that represents a base custom JFoenix alert.
 *
 * @author Hidayat Rzayev
 */
public abstract class CustomAlert {

    public static final String HEADING_MESSAGE_FONT = "Arial bold";
    public static final String CONTENT_MESSAGE_FONT = "Arial";
    public static final int HEADING_FONT_SIZE = 16;
    public static final int CONTENT_FONT_SIZE = 14;

    protected JFXAlert<ButtonType> alert;
    protected JFXDialogLayout dialogLayout;
    protected HBox heading;

    public CustomAlert() {
        alert = new JFXAlert<>();
        dialogLayout = new JFXDialogLayout();
        heading = new HBox(5);
        heading.setAlignment(Pos.CENTER_LEFT);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("ExploreHub");

        this.dialogLayout.getStylesheets().add("/Styles/alert.css");
        this.setHeading();
    }

    /**
     * Shows the alert on the given stack pane with the given content.
     *
     * @param content - content of the alert
     */
    public void showAlert(String content) {
        showAlertWithResponse(content, ButtonType.OK);
    }

    /**
     * Shows the alert on the given stack pane with the given content and button types.
     *
     * @param content - content of the alert
     * @param buttonTypes - types of button that should appear on the alert
     * @return the user's response (the button type that was clicked)
     */
    public Optional<ButtonType> showAlertWithResponse(String content, ButtonType... buttonTypes) {
        Label contentText = this.getAlertMessage(content, CONTENT_MESSAGE_FONT, CONTENT_FONT_SIZE);

        dialogLayout.setHeading(heading);
        dialogLayout.setBody(contentText);
        dialogLayout.setMaxWidth(400);

        alert.getDialogPane().getButtonTypes().clear();
        if (buttonTypes.length != 0) {
            this.addButtonTypes(buttonTypes);
        } else {
            this.addButtonTypes(ButtonType.OK, ButtonType.CANCEL);
        }

        alert.setContent(dialogLayout);
        alert.setAnimation(JFXAlertAnimation.TOP_ANIMATION);

        return alert.showAndWait();
    }

    /**
     * Creates and returns a {@link Label} containing the provided message.
     *
     * @param message - message that is to be shown on the alert
     * @param fontName - font of the label
     * @param fontSize - size of the font
     * @return the created {@link Label} object
     */
    public Label getAlertMessage(String message, String fontName, int fontSize) {
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(fontName, fontSize));
        return messageLabel;
    }

    /**
     * Adds the provided button types to the alert.
     * Any button click will close the alert and the alert
     * response will be set to that particular button type.
     *
     * @param buttonTypes - button types to be added
     */
    public void addButtonTypes(ButtonType... buttonTypes) {
        for (ButtonType buttonType : buttonTypes) {
            JFXButton button = new JFXButton(buttonType.getText());
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.setOnAction(event -> {
                alert.setAnimation(JFXAlertAnimation.NO_ANIMATION);
                alert.setResult(buttonType);
                alert.close();
            });
            button.setFocusTraversable(false);
            dialogLayout.getActions().add(button);
        }
    }

    /**
     * Sets the heading of the alert.
     * Since different alert types have different heading contents (icon and text),
     * this method is to be implemented by every alert type inheriting from this class.
     */
    public abstract void setHeading();
}
