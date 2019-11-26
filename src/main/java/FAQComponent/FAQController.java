package FAQComponent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.net.URL;
import java.util.ResourceBundle;

 /**
 * Frequently asked questions component
 * @author Daniel Dyankov
 */

public class FAQController implements Initializable {
    private WebEngine webengine;
    private static WebView webview;

    @FXML
    WebView FAQWebView;

     /**
      * This method is initializing the webview
      */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final WebEngine webEngine = FAQWebView.getEngine();


        try {
            webEngine.load("https://explorehub-faq.netlify.com");


        } catch (Exception e){
            System.out.println("FAQ not loading properly");
        }
    }
}

