package discussionComponent.views;

import authentification.loginProcess.CurrentAccountSingleton;
import discussionComponent.controllers.ViewTopicController;
import handlers.TimeConvertor;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    private Double listViewWidth;

    @FXML private AnchorPane topicNode;
    @FXML Text topicTitle, topicStarted, topicReplyCount, topicLastPostAuthor, topicLastPostDate;

    public TopicNode(Topic topic, ReadOnlyDoubleProperty listViewWidth){
        this.topic = topic;
        this.listViewWidth = listViewWidth.doubleValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/topicNode.fxml"));
        loader.setController(this);
        try {
            topicNode = loader.load();
            topicNode.setPrefWidth(this.listViewWidth);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/topicView.fxml"));

        ViewTopicController viewTopicController = new ViewTopicController(topic);
        loader.setController(viewTopicController);

        Scene scene = topicNode.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(1)).setVisible(false);
        AnchorPane categoryTopicDisplay = loader.load();
        contentController.getChildren().add(categoryTopicDisplay);
    }

    public AnchorPane getTopicNode(){ return topicNode; }


}
