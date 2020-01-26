package discussionComponent.views;

import javafx.scene.control.ListCell;
import models.Post;

public class PostListViewCell extends ListCell<Post> {
    @Override
    public void updateItem(Post post, boolean empty){
        super.updateItem(post, empty);
        if(post != null){
            PostNode postNode = new PostNode(post);

            setGraphic(postNode.getPostNode());
        }else{
            setGraphic(null);
        }
    }
}
