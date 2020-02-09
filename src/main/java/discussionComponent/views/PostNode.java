package discussionComponent.views;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import discussionComponent.controllers.EditPostController;
import handlers.Convenience;
import handlers.TimeConvertor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Account;
import models.Admin;
import models.Post;
import models.Topic;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.Optional;

public class PostNode {
    private Account currentUser = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager em = currentUser.getConnection();
    private Post post;

    @FXML private AnchorPane postNode;
    @FXML private JFXButton postEditButton, postDeleteButton;
    @FXML private Text postAuthor, postedTime, postLastEdited, postText;
    @FXML private Circle postUserImageCircle;

    public PostNode(Post post){
        this.post = post;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/postNode.fxml"));
        loader.setController(this);
        try {
            postNode = loader.load();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    void apply(){
        postDeleteButton.setVisible(false);
        postEditButton.setVisible(false);
        if(currentUser.getId() == post.getAuthor().getId() || currentUser instanceof Admin){
            postEditButton.setVisible(true);
            if(post.getPostID() != post.getTopic().getThreadFirstPost().getPostID()){
                postDeleteButton.setVisible(true);
            }
        }
        Image postUserImage = new Image(post.getAuthor().getPicture());
        postUserImageCircle.setFill(new ImagePattern(postUserImage));
        postedTime.setText(TimeConvertor.compareDate(post.getPostTime()));
        postAuthor.setText(post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname().substring(0,1) + ".");
        postText.setText(post.getPostContent());
        if(!post.getPostLastEdited().equals(post.getPostTime())){
           postLastEdited.setVisible(true);
           postLastEdited.setText("Last edited " + TimeConvertor.compareDate(post.getPostLastEdited()));
        }else{
            postLastEdited.setVisible(false);
        }

    }

    @FXML void editPost() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/postEditor.fxml"));
        EditPostController editPostController = new EditPostController(post);
        loader.setController(editPostController);

        Scene scene = postNode.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(2)).setVisible(false);
        contentController.getChildren().add(loader.load());
    }

    @FXML void deletePost(){
        Optional<ButtonType> confirmation = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Are you sure you want to delete this post? This action cannot be undone."
        );
        if(confirmation.isPresent() && confirmation.get() == ButtonType.OK){
            em.getTransaction().begin();
            Topic t = post.getTopic();
            Query query = em.createQuery("DELETE from Post p WHERE p.Id = :pid");
            int result = query.setParameter("pid", post.getPostID()).executeUpdate();

            query = em.createQuery("SELECT MAX(p.Id) FROM Post p WHERE p.topic = :tid");
            query.setParameter("tid", t);
            int lastPost = (Integer) query.getSingleResult();

            TypedQuery<Post> qp = em.createNamedQuery("Post.getPostById", Post.class);
            qp.setParameter("pid", lastPost);
            t.setThreadLastPost(qp.getSingleResult());

            em.merge(t);
            em.getTransaction().commit();
            Scene scene = postNode.getScene();
            AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
            ((Node) contentController.getChildren().get(2)).setVisible(false);
            ((Node) contentController.getChildren().get(2)).setVisible(true);
        }
    }

    public AnchorPane getPostNode(){ return postNode; }
}
