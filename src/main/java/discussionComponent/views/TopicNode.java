package discussionComponent.views;

import authentification.CurrentAccountSingleton;
import handlers.TimeConvertor;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Topic;

import javafx.fxml.FXML;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;

public class TopicNode {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private Topic topic;

    @FXML private AnchorPane topicNode;
    @FXML Text topicTitle, topicStarted, topicReplyCount, topicLastPostAuthor, topicLastPostDate;

    public TopicNode(Topic topic){
        this.topic = topic;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/topicNode.fxml"));
        loader.setController(this);
        try {
            topicNode = loader.load();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    void apply(){
        TypedQuery tq1 = em.createNamedQuery("Topic.getReplyCount", Integer.class);
        tq1.setParameter("tid", topic.getId());
        topicTitle.setText(topic.getThreadTitle());
        String s = "Started by " + topic.getThreadAuthor().getFirstname() + " " +
                topic.getThreadAuthor().getLastname().substring(0,1) + "., " +
                TimeConvertor.compareDate(topic.getThreadFirstPost().getPostTime());
        topicStarted.setText(s);
        topicReplyCount.setText(tq1.getSingleResult() + " Replies");
        topicLastPostAuthor.setText(topic.getThreadLastPost().getAuthor().getFirstname() +
                " " + topic.getThreadLastPost().getAuthor().getLastname().substring(0,1));
        topicLastPostDate.setText(TimeConvertor.compareDate(topic.getThreadLastPost().getPostTime()));
    }

    @FXML
    private void viewTopic() throws IOException{

    }

    public AnchorPane getTopicNode(){ return topicNode; }


}
