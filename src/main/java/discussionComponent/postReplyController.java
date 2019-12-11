package discussionComponent;

import authentification.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.Post;
import models.Thread;
import models.User;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class postReplyController implements Initializable {
    private Thread thread;

    @FXML
    private AnchorPane postAP;

    @FXML
    private Label postingTo;

    @FXML
    private TextArea replyMessage;

    private EntityManager entityManager;

    public postReplyController(Thread t){
        this.thread = t;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void goBackk(){
        ((Pane) postAP.getParent()).getChildren().get(0).setVisible(true);
        ((Pane) postAP.getParent()).getChildren().remove(postAP);
    }

    @FXML
    private void postReply() throws IOException{

        Post newPost = new Post(((User)CurrentAccountSingleton.getInstance().getAccount()),
                replyMessage.getText(),
                String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));

        newPost.setThread(thread);

        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
        entityManager.getTransaction().begin();
        entityManager.persist(newPost);
        thread.setThreadLastPost(newPost);
        entityManager.merge(thread);
        entityManager.getTransaction().commit();

        ((Pane) postAP.getParent()).getChildren().get(0).setVisible(true);
        ((Pane) postAP.getParent()).getChildren().remove(postAP);

    }


}
