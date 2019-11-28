package handlers;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

/**
 * This class contains the convenience methods for switching the scenes in the app,
 * showing alerts to the user, getting the window of the node or the event, etc.
 * @author Hidayat Rzayev
 */
public final class Convenience {

    /**
     * Switches the current scene to the specified scene.
     *
     * @param window - the main window where the scene should be displayed in
     * @param resource - location of the fxml file which is to be loaded
     * @throws IOException - may be thrown if the scene could not be loaded
     */
    public static void switchScene(Stage window, URL resource) throws IOException {
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

    /**
     * Creates a new {@link Alert} object.
     *
     * @param alertType - type of the alert
     * @param title - text contained in the title section of the alert
     * @param header - text contained in the header section of the alert
     * @param content - text contained in the content section of the alert
     * @return {@link Alert} object
     */
    private static Alert createAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Creates and shows a new alert to the user.
     *
     * @param alertType - type of the alert
     * @param title - text contained in the title section of the alert
     * @param header - text contained in the header section of the alert
     * @param content - text contained in the content section of the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = createAlert(alertType, title, header, content);
        alert.showAndWait();
    }

    /**
     * Creates and shows a new alert which can be responded to.
     *
     * @param alertType - type of the alert
     * @param title - text contained in the title section of the alert
     * @param header - text contained in the header section of the alert
     * @param content - text contained in the content section of the alert
     * @param buttonTypes - types of buttons to be displayed on the alert window.
     *                    By default (if the user has not specified any),
     *                    those are gonna be the "Ok" and "Cancel" buttons.
     * @return user's response (i.e. clicked button type)
     */
    public static Optional<ButtonType> showAlertWithResponse(Alert.AlertType alertType, String title, String header,
                                                             String content, ButtonType... buttonTypes) {
        Alert alert = createAlert(alertType, title, header, content);

        alert.getButtonTypes().clear();
        if (buttonTypes.length != 0) {
            alert.getButtonTypes().addAll(buttonTypes);
        } else {
            alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        }

        return alert.showAndWait();
    }

    /**
     * Pops up a new dialog window with a custom content.
     *
     * @param owner - window on which the dialog should appear
     * @param dialogResource - content of the dialog window
     * @param title - title of the dialog window
     * @throws IOException - may be thrown if the dialog could not be loaded
     */
    public static void popupDialog(Window owner, URL dialogResource, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(dialogResource);

        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        dialog.initOwner(owner);
        dialog.getDialogPane().setContent(loader.load());

        Window dialogWindow = dialog.getDialogPane().getScene().getWindow();
        dialogWindow.setOnCloseRequest(event -> dialogWindow.hide());
        dialog.showAndWait();
    }

    /**
     * Pops up a new dialog window after some kind of event.
     * Calls the {@link #popupDialog(Window, URL, String)} method by passing the event's window to it.
     *
     * @param event - event that triggered the dialog to popup
     * @param dialogResource - content of the dialog window
     * @param title - title of the dialog window
     * @throws IOException - may be thrown if the dialog could not be loaded
     */
    public static void popupDialog(Event event, URL dialogResource, String title) throws IOException {
        popupDialog(getWindow(event), dialogResource, title);
    }
}
