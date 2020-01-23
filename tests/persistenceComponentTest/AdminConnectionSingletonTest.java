package persistenceComponentTest;

import org.junit.Test;
import persistenceComponent.AdminConnectionSingleton;

/**
 * Test class for the {@link AdminConnectionSingleton}
 *
 * @author Gheorghe Mironica
 */
public class AdminConnectionSingletonTest {

    /**
     *
     * Test the method {@link #getManager()}
     */
    @Test
    public void getManager() {
        AdminConnectionSingleton ad = AdminConnectionSingleton.getInstance();
        ad.getManager();

    }
}