package discussionComponent;

import javafx.scene.control.ListCell;

public class ThreadListViewCell extends ListCell<ThreadListObject> {
    @Override
    public void updateItem(ThreadListObject tlo, boolean empty){
        super.updateItem(tlo, empty);
        if(tlo != null) {
            ThreadListElement threadListElement = new ThreadListElement();
            threadListElement.setElement(tlo);
            setGraphic(threadListElement.getListElement());
        }
    }
}
