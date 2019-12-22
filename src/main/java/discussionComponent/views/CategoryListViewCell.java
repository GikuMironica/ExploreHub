package discussionComponent.views;

import javafx.scene.control.ListCell;
import models.ForumCategory;

public class CategoryListViewCell extends ListCell<ForumCategory> {
    @Override
    public void updateItem(ForumCategory forumCategory, boolean empty){
        super.updateItem(forumCategory, empty);
        if(forumCategory != null){
            CategoryNode categoryNode = new CategoryNode(forumCategory);

            setGraphic(categoryNode.getCategoryNode());
        }else{
            setGraphic(null);
        }
    }
}
