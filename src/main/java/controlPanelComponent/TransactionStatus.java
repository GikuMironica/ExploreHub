package controlPanelComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration class which describes the transaction status
 *
 * @author Gheorghe Mironica
 */
public enum TransactionStatus {
        PENDING(0),
        ACCEPTED(1),
        REJECTED(2),
        CANCELED(3);

    private int number;
    private static Map map = new HashMap<>();

    TransactionStatus(int number) {
        this.number = number;
    }

    static {
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            map.put(transactionStatus.number, transactionStatus);
        }
    }

    /**
     * Get transaction status by inputing an int
     *
     * @param transactionType integer
     * @return Transaction status {@link models.Transactions}
     */
    public static TransactionStatus valueOf(int transactionType) {
        return (TransactionStatus) map.get(transactionType);
    }

    public int getValue() {
        return number;
    }
}
