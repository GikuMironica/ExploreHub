package authentification;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for the {@link UserConnectionSingleton}
 *
 * @author Gheorghe Mironica
 */
public class UserConnectionSingletonTest {

    /**
     * Test the functionality of the {@link #getManager() method}
     */
    @Test
    public void getManager() {
        UserConnectionSingleton ad = UserConnectionSingleton.getInstance();
        ad.getManager();
        assertTrue(!(ad.getManager()==null));
    }
}