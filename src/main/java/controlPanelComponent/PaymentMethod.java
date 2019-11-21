package controlPanelComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration class which describes the payment status
 *
 * @author Gheorghe Mironica
 */
public enum PaymentMethod {
    CARD(0),
    CASH(1);

    private int number;
    private static Map map = new HashMap<>();

    PaymentMethod(int number) {
        this.number = number;
    }

    static {
        for (PaymentMethod method : PaymentMethod.values()) {
            map.put(method.number, method);
        }
    }
    /**
     * Get payment status by inputing an int
     * @param payType integer
     * @return Transaction status {@link models.Transactions}
     */
    public static PaymentMethod valueOf(int payType) {
        return (PaymentMethod) map.get(payType);
    }

    public int getValue() {
        return number;
    }
}
