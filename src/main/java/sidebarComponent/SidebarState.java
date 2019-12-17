package sidebarComponent;

import handlers.Convenience;
import handlers.IniHandler;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * A singleton class which is responsible for saving/retrieving the state of the sidebar
 * to/from the corresponding configuration file.
 * @author Hidayat Rzayev
 */
public class SidebarState {

    /**
     * Saves the current state of the sidebar
     *
     * @param isHidden - whether or not the sidebar is hidden
     */
    public static void saveStateHidden(boolean isHidden) {
        IniHandler iniHandler = new IniHandler("config.ini");
        try {
            iniHandler.updateSection("sidebar", "hidden", isHidden);
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Gets the current state of the sidebar
     *
     * @return {@code true} if the sidebar is hidden, otherwise - {@code false}
     */
    public static boolean getStateHidden() {
        IniHandler iniHandler = new IniHandler("config.ini");
        try {
            return iniHandler.readSection("sidebar", "hidden", boolean.class);
        } catch (IOException ioe) {
            return true;
        }
    }
}
