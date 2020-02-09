package discussionComponent.views;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Topic;

public class TopicListViewCell extends ListCell<Topic> {
    private ListView lv;
    public TopicListViewCell (ListView lv){
        this.lv = lv;
    }
    @Override
    public void updateItem(Topic topic, boolean empty){
        super.updateItem(topic, empty);
        if(topic != null){
            TopicNode topicNode = new TopicNode(topic, this.lv.widthProperty());
            topicNode.apply();
            setGraphic(topicNode.getTopicNode());
        }else{
            setGraphic(null);
        }

    }
}
