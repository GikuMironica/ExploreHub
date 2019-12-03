package discussionComponent;
import models.Topic;
import javafx.scene.control.ListCell;

public class ThreadListViewCell extends ListCell<Topic> {
    @Override
    public void updateItem(Topic tlo, boolean empty){
        super.updateItem(tlo, empty);
        if(tlo != null) {
            ThreadListElement threadListElement = new ThreadListElement();
            threadListElement.setElement(tlo);
            setGraphic(threadListElement.getListElement());
        }
    }
}
