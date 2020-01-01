package alerts;

/**
 * A factory class, which is responsible for creating
 * specific custom alerts based on the given type.
 *
 * @author Hidayat Rzayev
 */
public class CustomAlertFactory {

    /**
     * Creates and returns a custom alert of the provided type.
     *
     * @param alertType - type of the alert to be created
     * @return a new custom alert object
     */
    public CustomAlert createAlert(CustomAlertType alertType) {
        switch (alertType) {
            case INFORMATION:
                return new CustomAlertInformation();
            case CONFIRMATION:
                return new CustomAlertConfirmation();
            case SUCCESS:
                return new CustomAlertSuccess();
            case WARNING:
                return new CustomAlertWarning();
            case ERROR:
                return new CustomAlertError();
            default:
                return null;
        }
    }
}
