package discussionComponent;

import authentification.CurrentAccountSingleton;
import com.sandec.mdfx.MDFXNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Admin;
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
    private ImageView author_image;

    @FXML
    private Label posted_time;

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;


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
        if(CurrentAccountSingleton.getInstance().getAccount().getId() == p.getAuthor().getId() || CurrentAccountSingleton.getInstance().getAccount() instanceof Admin){
            btnEdit.setVisible(true);
            if(p.getTopic().getThreadFirstPost().getPostID() != p.getPostID()){
                btnDelete.setVisible(true);
            }
        }
        MDFXNode mdfxNode = new MDFXNode(p.getPostContent());
        mdfxNode.getStylesheets().add("/Styles/post.css");
        postAP.getChildren().add(mdfxNode);
        posted_time.setText(p.getPostTime());
        posterName.setText(p.getAuthor().getFirstname());
        author_image.setImage(new Image(p.getAuthor().getPicture()));
    }

    public AnchorPane getPostElement(){
        return postElement;
    }
}
