package discussionComponent;
import models.Thread;
import javafx.scene.control.ListCell;

public class ThreadListViewCell extends ListCell<Thread> {
    @Override
    public void updateItem(Thread tlo, boolean empty){
        super.updateItem(tlo, empty);
        if(tlo != null) {
            ThreadListElement threadListElement = new ThreadListElement();
            threadListElement.setElement(tlo);
            setGraphic(threadListElement.getListElement());
        }
    }
}
