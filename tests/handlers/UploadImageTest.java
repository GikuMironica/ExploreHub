package handlers;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;

/**
 * Test class for UploadImage, using TestFX framework
 *
 * @author Gheorghe Mironica
 */
@ExtendWith(ApplicationExtension.class)
class UploadImageTest{
    private Image img;
    private UploadImage uploadImage;
    private final String IMGUR_PATTERN = "https:\\/\\/i.imgur.com\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*.png";
    private final String IMGUR_API = "https://api.imgur.com/3/image";
    private URLConnection connection;

    /**
     * Setup method, mocks some UI, tries to ping Imgur server to check if the API URL is still valid
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    private void start(Stage stage) {
        try {
            connection = new URL(IMGUR_API).openConnection();
            connection.connect();

            img = new Image("/IMG/quest.png");
            uploadImage = new UploadImage(img);
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }

        stage.setScene(new Scene(new StackPane(new ImageView(img)), 100, 100));
        stage.show();
    }

    /**
     * Test method which validates the functionality of {@link UploadImage} upload method.
     * Should return upload the image with post request, get back a valid URL for the image.
     */
    @Test
    void upload() {
        try {
            String imageURL;
            imageURL = uploadImage.upload();
            assertTrue(imageURL.matches(IMGUR_PATTERN));
            assertFalse(img==null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}