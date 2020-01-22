package bookingComponent;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controlPanelComponent.PaymentMethod;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;



public class PaymentCardController implements Initializable {

    @FXML
    JFXTextField iban;
    @FXML
    JFXTextField bic;
    @FXML
    JFXPasswordField pin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public String getIban(){
        return iban.getText();
    }

    public String getBic(){
        return bic.getText();
    }

    public String getPin(){
        return pin.getText();
    }

}
