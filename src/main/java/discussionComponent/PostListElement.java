package discussionComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.sandec.mdfx.MDFXNode;
import handlers.Convenience;
import handlers.TimeConvertor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Admin;
import models.Post;
import models.Topic;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.Optional;

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

    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();


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
        posted_time.setText(TimeConvertor.compareDate(p.getPostTime()));
        posterName.setText(p.getAuthor().getFirstname());
        author_image.setImage(new Image(p.getAuthor().getPicture()));
        btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deletePost(p);
            }
        });
    }

    private void deletePost(Post p){
        Optional<ButtonType> a = Convenience.showAlertWithResponse(CustomAlertType.CONFIRMATION,
                "Are you sure you want to delete this post? This action cannot be undone.");
        if(a.isPresent() && a.get() == ButtonType.OK){
            em.getTransaction().begin();
            Topic t = p.getTopic();
            Query query = em.createQuery(
                    "DELETE FROM Post p WHERE p.Id = :pid");
            int deletedCount = query.setParameter("pid", p.getPostID()).executeUpdate();

            Query q = em.createQuery("SELECT MAX(p.Id) FROM Post p WHERE p.topic = :tid");
            q.setParameter("tid", t);
            int lastPost = (Integer) q.getSingleResult();

            TypedQuery<Post> qp = em.createNamedQuery("Post.getPostById", Post.class);
            qp.setParameter("pid", lastPost);
            t.setThreadLastPost(qp.getSingleResult());
            em.merge(t);

            em.getTransaction().commit();

            threadViewController.threadListView.getItems().remove(p);
        }
    }

    public AnchorPane getPostElement(){
        return postElement;
    }
}
