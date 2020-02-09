package discussionComponent.controllers;

import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import discussionComponent.views.TopicListViewCell;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ForumCategory;
import models.Topic;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewCategoryController implements Initializable {
    private ForumCategory forumCategory;
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();

    @FXML AnchorPane catAP;
    @FXML Text categoryTitle;
    @FXML JFXButton addTopicBtn;
    @FXML VBox vcVbox;

    private List<Topic> topicList;
    private ObservableList<Topic> topicObservableList;

    public ViewCategoryController(ForumCategory fc){
        this.forumCategory = fc;
    }

    public void initialize(URL url, ResourceBundle rb){
        categoryTitle.setText(forumCategory.getName());
        TypedQuery<Topic> tq1 = em.createNamedQuery("Topic.getThreadsbyForum", Topic.class);
        tq1.setParameter("fName", forumCategory.getName());
        ListView<Topic> topicListView = new ListView<>();
        refreshListView(topicListView, tq1);
        Platform.runLater(() -> vcVbox.getChildren().add(topicListView));
        catAP.visibleProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                refreshListView(topicListView, tq1);
            }
        }));
    }

    private void refreshListView(ListView topicListView, TypedQuery<Topic> tq1){
        Platform.runLater(() -> {
            topicListView.getItems().clear();
            topicList = tq1.getResultList();
            topicObservableList = FXCollections.observableArrayList();
            topicObservableList.setAll(topicList);
            topicListView.setItems(topicObservableList);
            topicListView.setCellFactory(param -> new TopicListViewCell(topicListView));
            topicListView.prefHeightProperty().bind(Bindings.size(topicObservableList).multiply(106));
        });
    }

    @FXML private void addTopic() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/postEditor.fxml"));
        NewTopicController newTopicController = new NewTopicController(forumCategory);
        loader.setController(newTopicController);
        Scene scene = categoryTitle.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(1)).setVisible(false);
        contentController.getChildren().add(loader.load());
    }

    @FXML private void returnToHome(){
        Scene scene = categoryTitle.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(0)).setVisible(true);
        contentController.getChildren().remove(1);
    }

}
