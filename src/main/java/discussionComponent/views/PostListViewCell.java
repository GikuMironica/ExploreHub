package discussionComponent.views;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Post;

public class PostListViewCell extends ListCell<Post> {
    private ListView listView;
    public PostListViewCell(ListView listView){
        this.listView = listView;
    }

    @Override
    public void updateItem(Post post, boolean empty){
        super.updateItem(post, empty);
        if(post != null){
            PostNode postNode = new PostNode(post, this.listView.widthProperty());
            postNode.apply();
            setGraphic(postNode.getPostNode());
        }else{
            setGraphic(null);
        }
    }
}
