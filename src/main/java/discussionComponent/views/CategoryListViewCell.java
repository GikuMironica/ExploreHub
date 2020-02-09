package discussionComponent.views;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.ForumCategory;

public class CategoryListViewCell extends ListCell<ForumCategory> {
    private ListView listView;

    public CategoryListViewCell(ListView lv){
        this.listView = lv;
    }
    @Override
    public void updateItem(ForumCategory forumCategory, boolean empty){
        super.updateItem(forumCategory, empty);
        if(forumCategory != null){
            CategoryNode categoryNode = new CategoryNode(forumCategory, listView.widthProperty());
            categoryNode.setElement();
            setGraphic(categoryNode.getCategoryNode());
        }else{
            setGraphic(null);
        }
    }
}
