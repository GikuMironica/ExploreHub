package discussionComponent.views;

import javafx.scene.control.ListCell;
import models.Topic;

public class TopicListViewCell extends ListCell<Topic> {
    @Override
    public void updateItem(Topic topic, boolean empty){
        super.updateItem(topic, empty);
        if(topic != null){
            TopicNode topicNode = new TopicNode(topic);
            topicNode.apply();
            setGraphic(topicNode.getTopicNode());
        }else{
            setGraphic(null);
        }

    }
}
