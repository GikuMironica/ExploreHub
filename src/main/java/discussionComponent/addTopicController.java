package discussionComponent;

import authentification.loginProcess.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.*;
import models.Topic;

import javax.persistence.EntityManager;
import java.net.URL;
import java.sql.Timestamp;
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
            Post newPost = new Post(user, message.getText(), String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
            Topic newTopic = new Topic(fp, topic.getText(), user, 0, 0);

            entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();

            entityManager.getTransaction().begin();

            entityManager.persist(newTopic);
            newPost.setTopic(newTopic);

            entityManager.persist(newPost);

            newTopic.setThreadFirstPost(newPost);
            newTopic.setThreadLastPost(newPost);

            entityManager.merge(newTopic);
            entityManager.getTransaction().commit();

            discussionController.topicListElementSet.add(newTopic);
            ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
        }
    }
    @FXML
    private void cancel(MouseEvent mouseEvent){
        ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
    }


}
