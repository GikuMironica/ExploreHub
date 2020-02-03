package discussionComponent.views;

import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Account;
import models.Admin;
import models.Post;

import javax.persistence.EntityManager;

public class PostNode {
    private Account currentUser = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager em = currentUser.getConnection();
    private Post post;

    @FXML private AnchorPane postNode;
    @FXML private JFXButton postEditButton, postDeleteButton;
    @FXML private Label postAuthor, postedTime, postLastEdited;
    @FXML private ImageView postAuthorImage;

    public PostNode(Post post){
        this.post = post;
    }

    private void apply(){
        if(currentUser.getId() == post.getAuthor().getId() || currentUser instanceof Admin){
            postEditButton.setVisible(true);
        }
        if(currentUser instanceof Admin) postDeleteButton.setVisible(true);
    }

    public AnchorPane getPostNode(){ return postNode; }
}
