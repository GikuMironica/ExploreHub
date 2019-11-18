package authentification;

import org.junit.Test;

import static org.junit.Assert.*;

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