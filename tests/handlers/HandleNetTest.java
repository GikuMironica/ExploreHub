package handlers;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link HandleNet}
 * @author Gheorghe Mironica
 */
public class HandleNetTest {

    /**
     * Test method which checks the functionality of the {@link #hasNetConnection()}
     * This is a synchronized method which polls in a parallel task some servers
     * and thus verifies if user has internet connection
     */
    @Test
    public void hasNetConnection() {
        boolean hasNet;
        String url = "https://www.linkedin.com/";
        hasNet = HandleNet.hasNetConnection();

        if(hasNet){
            assertTrue(hasNet);
        } else {
            HandleNet.setUrl1(url);
            hasNet = HandleNet.hasNetConnection();
            assertTrue(hasNet);
        }
    }
}