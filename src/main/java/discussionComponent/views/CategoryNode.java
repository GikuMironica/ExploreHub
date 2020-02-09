package discussionComponent.views;

import authentification.loginProcess.CurrentAccountSingleton;
import discussionComponent.controllers.ViewCategoryController;
import handlers.TimeConvertor;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.ForumCategory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;

import javafx.fxml.FXML;
import models.Post;

public class CategoryNode {
    @FXML private AnchorPane categoryNode;
    @FXML private Label categoryTitle, topicCount, postCount, recentPostAuthor, recentTopicTitle, recentPostTime;

    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private ForumCategory category;
    private Double listViewWidth;

    public CategoryNode(ForumCategory category, ReadOnlyDoubleProperty listViewWidth){
        this.category = category;
        this.listViewWidth = listViewWidth.doubleValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/categoryNode.fxml"));
        loader.setController(this);
        try {
            categoryNode = loader.load();
            categoryNode.setMinWidth(this.listViewWidth);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    void setElement(){
        categoryTitle.setText(category.getName());
        TypedQuery<Integer> typedQuery = em.createNamedQuery("ForumCategory.getTopicCount", Integer.class);
        typedQuery.setParameter("f", category);
        topicCount.setText(String.valueOf(typedQuery.getSingleResult()) + " Topics");
        typedQuery = em.createNamedQuery("ForumCategory.getReplyCount", Integer.class);
        typedQuery.setParameter("f", category);
        postCount.setText(typedQuery.getSingleResult() + " Posts");
        TypedQuery<Post> typedQuery1 = em.createNamedQuery("Post.getLastPost", Post.class);
        typedQuery1.setParameter("f", category);
        if(typedQuery1.getResultList().size() > 0) {
            Post lastPost = typedQuery1.getResultList().get(0);
            recentPostAuthor.setText(lastPost.getAuthor().getFirstname());
            recentTopicTitle.setText(lastPost.getTopic().getThreadTitle());
            recentPostTime.setText(TimeConvertor.compareDate(lastPost.getPostTime()));
        }
    }

    @FXML
    private void viewCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/categoryView.fxml"));

        ViewCategoryController viewCategoryController = new ViewCategoryController(category);
        loader.setController(viewCategoryController);

        Scene scene = categoryNode.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(0)).setVisible(false);
        AnchorPane categoryTopicDisplay = loader.load();
        contentController.getChildren().add(categoryTopicDisplay);
    }

    public AnchorPane getCategoryNode() { return categoryNode; }

}
