package FAQComponent;

import handlers.Convenience;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Frequently asked questions component
 * @author Daniel Dyankovs
 */

public class FAQController implements Initializable {
    private WebEngine webengine;
    private static WebView webview;

    @FXML
    WebView FAQWebView;

    /**
     * This method initializes the webview
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final WebEngine webEngine = FAQWebView.getEngine();


        try {
            webEngine.load("https://explorehubfaq.netlify.com/");


        } catch (Exception e){
            System.out.println("FAQ not loading properly");
        }

    }

}