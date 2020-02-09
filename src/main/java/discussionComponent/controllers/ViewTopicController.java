package discussionComponent.controllers;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import discussionComponent.views.PostListViewCell;
import handlers.Convenience;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Account;
import models.Admin;
import models.Post;
import models.Topic;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewTopicController implements Initializable {
    private Topic topic;
    private Account currentUser = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager em = currentUser.getConnection();

    @FXML private AnchorPane topicViewAP;
    @FXML private VBox topicVBox;
    @FXML private Text topicTitle;
    @FXML private JFXButton backToTopicListBtn, deleteTopicBtn, deleteTopicBtn1;

    private List<Post> postList;
    private ObservableList<Post> postObservableList;

    public ViewTopicController(Topic topic){
        this.topic = topic;
    }

    public void initialize(URL url, ResourceBundle rb){
        if(currentUser instanceof Admin){
            deleteTopicBtn.setVisible(true);
            deleteTopicBtn1.setVisible(true);
        }else{
            deleteTopicBtn1.setVisible(false);
            deleteTopicBtn.setVisible(false);
        }
        System.out.println(topic.getThreadTitle());
        TypedQuery<Post> tq1 = em.createNamedQuery("Post.getPostbyThread", Post.class);
        tq1.setParameter("t", this.topic);
        topicTitle.setText(topic.getThreadTitle());
        ListView<Post> postListView = new ListView<>();
        postListView.getStylesheets().add("/Styles/postList.css");
        refreshPostList(postListView, tq1);
        Platform.runLater(()-> topicVBox.getChildren().add(2, postListView));
        topicViewAP.visibleProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                refreshPostList(postListView, tq1);
            }
        }));
    }

    private void getPostList(TypedQuery<Post> tq1){
        postList = tq1.getResultList();
    }

    private void refreshPostList(ListView postListView, TypedQuery<Post> tq1){
        Platform.runLater(() -> {
            getPostList(tq1);
            postListView.getItems().clear();
            postObservableList = FXCollections.observableArrayList();
            postObservableList.setAll(postList);
            postListView.setItems(postObservableList);
            postListView.setCellFactory(param -> new PostListViewCell(postListView));
            postListView.prefHeightProperty().bind(Bindings.size(postObservableList).multiply(180));

            postListView.refresh();
        });
    }

    @FXML void back(){
        Scene scene = topicViewAP.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        contentController.getChildren().remove(2);
        ((Node) contentController.getChildren().get(1)).setVisible(true);
    }

    @FXML private void deleteTopic(){
        Optional<ButtonType> confirmation = Convenience.showAlertWithResponse(
                CustomAlertType.CONFIRMATION,
                "Are you sure you want to delete this topic? This action cannot be undone."
        );
        if(confirmation.isPresent() && confirmation.get() == ButtonType.OK){
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE from Post p WHERE p.topic = :t");
            int result = query.setParameter("t", topic).executeUpdate();

            query = em.createQuery("DELETE from Topic t WHERE t.Id = :tid");
            query.setParameter("tid", topic.getId()).executeUpdate();

            em.getTransaction().commit();
            back();
        }
    }

    @FXML private void addPost() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/postEditor.fxml"));
        NewPostController newPostController = new NewPostController(topic);

        loader.setController(newPostController);
        Scene scene = topicTitle.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(2)).setVisible(false);
        contentController.getChildren().add(loader.load());
    }
}
