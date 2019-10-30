package authentification;

import org.junit.Test;

import static org.junit.Assert.*;

public class AdminConnectionSingletonTest {

    @Test
    public void getManager() {
        AdminConnectionSingleton ad = AdminConnectionSingleton.getInstance();
        ad.getManager();

    }
}