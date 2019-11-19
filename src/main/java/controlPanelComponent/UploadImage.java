package controlPanelComponent;

import javafx.scene.control.PasswordField;

import org.apache.commons.codec.binary.Base64;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadImage {

    private Image img;
    private final static String API_KEY = "4f8b5205a0f38e9";
    private final String IMGUR_PATTER = "https:\\\\/\\\\/i.imgur.com\\\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*.png";

    UploadImage(Image oneImage){
        img = oneImage;
    }

    /**
     * Uploads the images to imugr and returns the link
     *
     * @return URL String
     */
    public String upload() throws Exception {

        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedImage image = SwingFXUtils.fromFXImage(img, null);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encodeBase64String(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(dataImage, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + API_KEY);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");


        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        String message ="";
        Pattern pattern = Pattern.compile(IMGUR_PATTER);
        Matcher matcher = pattern.matcher(stb.toString());
        if (matcher.find())
        {
            message = matcher.group();
        }

        message = message.replace("\\","");
        System.out.println(message);
        return message;
    }
}
