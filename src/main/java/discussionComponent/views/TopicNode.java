package discussionComponent.views;

import authentification.CurrentAccountSingleton;
import handlers.TimeConvertor;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Topic;

import javafx.fxml.FXML;
import javax.persistence.EntityManager;
import java.io.IOException;

public class TopicNode {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private Topic topic;

    @FXML private AnchorPane topicNode;
    @FXML private Text topicTitle, topicStarted, topicReplyCount, topicLastPostAuthor, topicLastPostDate;

    public TopicNode(Topic topic){
        this.topic = topic;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/topicNode.fxml"));
        try {
            topicNode = loader.load();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void apply(){
        topicTitle.setText(topic.getThreadTitle());
        String s = "Started by " + topic.getThreadAuthor().getFirstname() + ", " + TimeConvertor.compareDate(topic.getThreadFirstPost().getPostTime());
        topicStarted.setText(s);
    }

    @FXML
    private void viewTopic() throws IOException{

    }

    public AnchorPane getTopicNode(){ return topicNode; }


}
