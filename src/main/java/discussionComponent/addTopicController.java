package discussionComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.*;
import models.Thread;

import javax.persistence.EntityManager;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class addTopicController implements Initializable {
    @FXML
    private Label postingEventTo;

    @FXML
    private TextField topic;

    @FXML
    private TextArea message;

    @FXML
    private AnchorPane createTopicAP;

    private ForumCategory fp;
    private Account user = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager entityManager;

    public addTopicController(ForumCategory fp){
        this.fp = fp;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postingEventTo.setText("Posting Topic to " + this.fp.getName());
    }

    @FXML void postTopic(MouseEvent mouseEvent){
        if(message.getText().length() > 10){
            Post newPost = new Post((User)user, message.getText(), String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
            Thread newThread = new Thread(fp, topic.getText(), (User) user, 0, 0);

            entityManager = UserConnectionSingleton.getInstance().getManager();
            entityManager.getTransaction().begin();
            entityManager.persist(newThread);
            entityManager.getTransaction().commit();

            newPost.setThread(newThread);


            entityManager.getTransaction().begin();
            entityManager.persist(newPost);
            entityManager.getTransaction().commit();

            entityManager.getTransaction().begin();
            newThread.setThreadFirstPost(newPost);
            newThread.setThreadLastPost(newPost);
            entityManager.merge(newThread);
            entityManager.getTransaction().commit();

            discussionController.threadListElementSet.add(newThread);
            ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
        }
    }
    @FXML
    private void cancel(MouseEvent mouseEvent){
        ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
    }


}
