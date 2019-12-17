package discussionComponent;

import handlers.time;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Post;

import java.io.IOException;

public class threadPostElement {
    @FXML
    private AnchorPane postElement;
    @FXML
    private Label posterName;
    @FXML
    private Label posted_time;
    @FXML
    private Label postContent;
    @FXML
    private ImageView author_image;

    public threadPostElement() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/postObject.fxml"));
        fxmlLoader.setController(this);
        try {
            postElement = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setElement(Post post) {
        posterName.setText(post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname().substring(0,1));
        postContent.setText(post.getPostContent());
        posted_time.setText(time.compareDate(post.getPostTime()));
    }

    public AnchorPane getListElement() {
        return postElement;
    }
}