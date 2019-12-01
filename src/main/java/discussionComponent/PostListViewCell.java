package discussionComponent;

import javafx.scene.control.ListCell;
import models.Post;

public class PostListViewCell extends ListCell<Post> {
    @Override
    public void updateItem(Post tlo, boolean empty){
        super.updateItem(tlo, empty);
        if(tlo != null) {
            PostListElement postListElement = new PostListElement();
            postListElement.setPostElement(tlo);
            setGraphic(postListElement.getPostElement());
        }
    }
}