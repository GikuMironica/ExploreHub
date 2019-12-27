package authentification;

import handlers.Convenience;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import org.apache.commons.beanutils.ConversionException;

import java.net.URL;
import java.util.ResourceBundle;

public class SuccessRegister implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Thread thread1 = new Thread(()->
        {
            PauseTransition visiblePause = new PauseTransition(
                    Duration.seconds(2)
            );
            visiblePause.setOnFinished(
                    (ActionEvent ev) -> {
                        Convenience.closePreviousDialog();
                    }
            );
            visiblePause.play();
        });
    //    thread1.start();
    }
}
