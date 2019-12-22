package discussionComponent.views;

import authentification.CurrentAccountSingleton;
import javafx.scene.layout.AnchorPane;
import models.Topic;

import javafx.fxml.FXML;
import javax.persistence.EntityManager;
import java.io.IOException;

public class TopicNode {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private Topic topic;

    @FXML private AnchorPane topicNode;

    public TopicNode(Topic topic){
        this.topic = topic;

    }

    @FXML
    private void viewTopic() throws IOException{

    }

    public AnchorPane getTopicNode(){ return topicNode; }


}
