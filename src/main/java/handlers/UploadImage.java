package handlers;

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

    private Image image;
    private final static String API_KEY = "4f8b5205a0f38e9";
    private final String IMGUR_PATTERN = "https:\\\\/\\\\/i.imgur.com\\\\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*.png";
    private final String IMGUR_API = "https://api.imgur.com/3/image";

    public UploadImage(Image oneImage){
        image = oneImage;
    }

    /**
     * Uploads the images to imugr and returns the link
     *
     * @throws Exception
     * @return URL String
     */
    public String upload() throws Exception {

        URL url = new URL(IMGUR_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        BufferedImage localImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(localImage, "png", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encodeBase64String(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(dataImage, "UTF-8");

        post(conn);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        StringBuilder stb = get(wr, conn);

        String message ="";
        Pattern pattern = Pattern.compile(IMGUR_PATTERN);
        Matcher matcher = pattern.matcher(stb.toString());
        if (matcher.find())
        {
            message = matcher.group();
        }

        return message.replace("\\","");
    }

    private StringBuilder get(OutputStreamWriter wr, HttpURLConnection conn) {
        StringBuilder stb = new StringBuilder();
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                stb.append(line).append("\n");
            }
            wr.close();
            rd.close();
            return stb;

        } catch(Exception e){
            return stb;
        }
    }

    private void post(HttpURLConnection conn) {
        try {
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + API_KEY);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

        }catch(Exception e){
            //
        }
    }

    public void setImage(Image img) {
        this.image = img;
    }
}
