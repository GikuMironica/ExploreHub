package discussionComponent;

import com.sandec.mdfx.MDFXNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Post;

import java.io.IOException;

public class PostListElement {
    @FXML
    private AnchorPane postElement;

    @FXML
    private Label posterName;

    @FXML
    private AnchorPane postAP;

    @FXML
    private Label posted_time;


    public PostListElement(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/postObject.fxml"));
        fxmlLoader.setController(this);
        try{
            postElement = fxmlLoader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setPostElement(Post p){
        MDFXNode mdfxNode = new MDFXNode(p.getPostContent());
        mdfxNode.getStylesheets().add("/Styles/post.css");
        postAP.getChildren().add(mdfxNode);
        posted_time.setText(p.getPostTime());
        posterName.setText(p.getAuthor().getFirstname());
    }

    public AnchorPane getPostElement(){
        return postElement;
    }
}
