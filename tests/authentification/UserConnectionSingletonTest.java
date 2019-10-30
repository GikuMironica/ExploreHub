package authentification;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserConnectionSingletonTest {

    @Test
    public void getManager() {
        UserConnectionSingleton ad = UserConnectionSingleton.getInstance();
        ad.getManager();
    }
}