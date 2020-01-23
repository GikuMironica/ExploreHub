package handlers;

import javafx.scene.control.Alert;

import java.net.URL;
import java.net.URLConnection;

/**
 * This class checks whether users has internet connection or not
 *
 * @author Gheorghe Mironica
 */
public class HandleNet {

    private static String url1 = "http://www.google.com";

    /**
     * This method pings www.google.com and returns true if everything is fine
     * if false -> user has no internet connection
     * @return {@link Boolean}
     */
    public static boolean hasNetConnection() {
        try {
            final URL url = new URL(url1);
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static void setUrl1(String url) {
        url1 = url;
    }
}
