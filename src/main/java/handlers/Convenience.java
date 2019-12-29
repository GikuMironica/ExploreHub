package handlers;

import alerts.*;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * This class contains the convenience methods for switching the scenes in the app,
 * showing alerts to the user, getting the window of the node or the event, etc.
 * @author Hidayat Rzayev
 */
public final class Convenience {

    private static JFXDialog previousDialog;

    /**
     * Switches the current scene to the specified scene.
     *
     * @param window - the main window where the scene should be displayed in
     * @param resource - location of the fxml file which is to be loaded
     * @throws IOException - may be thrown if the scene could not be loaded
     */
    public static void switchScene(Stage window, URL resource) throws IOException {
        window.close();
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    /**
     * Switches the current scene to the specified scene.
     * Gets the window of the event and calls {@link #switchScene(Stage, URL)}
     *
     * @param event - the event which invoked the change of scene
     * @param resource - location of the fxml file which is to be loaded
     * @throws IOException - may be thrown if the scene could not be loaded
     */
    public static void switchScene(Event event, URL resource) throws IOException {
        switchScene(getWindow(event), resource);
    }

    /**
     * Switches the current scene to the specified scene.
     * Gets the window of the node and calls {@link #switchScene(Stage, URL)}
     *
     * @param node - any node which is located in the current scene
     * @param resource - location of the fxml file which is to be loaded
     * @throws IOException - may be thrown if the scene could not be loaded
     */
    public static void switchScene(Node node, URL resource) throws IOException {
        switchScene(getWindow(node), resource);
    }

    /**
     * Gets the window of the provided event.
     *
     * @param event - event the window of which is to be fetched
     * @return {@link Stage} object (the fetched window)
     */
    public static Stage getWindow(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    /**
     * Gets the window of the provided node.
     *
     * @param node - node the window of which is to be fetched
     * @return {@link Stage} object (the fetched window)
     */
    public static Stage getWindow(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static void showAlert(CustomAlertType alertType,  String content) {
        CustomAlertFactory alertFactory = new CustomAlertFactory();
        CustomAlert alert = alertFactory.createAlert(alertType);
        alert.showAlert(content);
    }

    /**
     * Creates and shows a new alert which can be responded to.
     *
     * @param alertType - type of the alert
     * @param content - text contained in the content section of the alert
     * @param buttonTypes - types of buttons to be displayed on the alert window.
     *                    By default (if the user has not specified any),
     *                    those are gonna be the "Ok" and "Cancel" buttons.
     * @return user's response (i.e. clicked button type)
     */
    public static Optional<ButtonType> showAlertWithResponse(CustomAlertType alertType, String content,
                                                             ButtonType... buttonTypes) {
        CustomAlertFactory alertFactory = new CustomAlertFactory();
        CustomAlert alert = alertFactory.createAlert(alertType);
        return alert.showAlertWithResponse(content, buttonTypes);
    }

    /**
     * Pops up a new dialog window with a custom content.
     *
     * @param stackPane - pane on which the dialog should appear
     * @param paneToBlur - pane on which the blur effect should appear
     * @param dialogResource - content of the dialog windows
     * @throws IOException - may be thrown if the dialog could not be loaded
     */
    public static <T> T popupDialog(StackPane stackPane, Pane paneToBlur, URL dialogResource) throws IOException {
        closePreviousDialog();

        BoxBlur blur = new BoxBlur(3, 3, 3);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(dialogResource);
        Parent parent = loader.load();

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setBody(parent);

        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        dialog.setOnDialogClosed(event -> paneToBlur.setEffect(null));
        dialog.setOnDialogOpened(event -> paneToBlur.setEffect(blur));
        dialog.show();

        previousDialog = dialog;

        return loader.getController();
    }

    /**
     * Closes the previously opened dialog.
     */
    public static void closePreviousDialog() {
        if (previousDialog != null) {
            previousDialog.close();
        }
    }

    public static JFXDialog getDialog(){
        return previousDialog;
    }
}
