package discussionComponent;

import authentification.loginProcess.CurrentAccountSingleton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import models.*;
import models.Topic;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 *  Discussion controller for ExploreHub
 *  @author Braz Castana (3126778)
 */

public class discussionController implements Initializable {
    /**
     * @FXML declarations
     */
    @FXML private Pane view;
    @FXML private Button addTopicBtn;
    @FXML private ChoiceBox eventDiscussion;
    @FXML private Label postingEventTo;

    ObservableList observableList = FXCollections.observableArrayList();
    ObservableList forumObservableList = FXCollections.observableArrayList();
    private ListView threadListView;
    public static List<Topic> topicListElementSet = new ArrayList<>();

    private Account user = CurrentAccountSingleton.getInstance().getAccount();
    private EntityManager entityManager;
    private static List<ForumCategory> tempList;

    private void setListView(){
        threadListView.getItems().clear();
        observableList.setAll(topicListElementSet);
        threadListView.setItems(observableList);
        threadListView.setCellFactory((Callback<ListView<Topic>, ListCell<Topic>>) param -> new ThreadListViewCell());
    }

    private void initThreadListView(){
        threadListView = new ListView();
        threadListView.setMinWidth(600.0);
        TypedQuery<Topic> tq1 = entityManager.createNamedQuery("Topic.getThreadsbyForum", Topic.class);
        tq1.setParameter("fName", eventDiscussion.getSelectionModel().getSelectedItem());
        Platform.runLater(() -> {
            topicListElementSet = tq1.getResultList();
            setListView();
            if(addTopicBtn.isDisabled()) addTopicBtn.setDisable(false);
            view.getChildren().add(threadListView);

        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManager = user.getConnection();


        if (user instanceof User) {
            TypedQuery<ForumCategory> tq1 = entityManager.createNamedQuery("ForumCategory.getCategories", ForumCategory.class);
            tq1.setParameter("fname", ((User) user).getCourse().getName());
            tempList = tq1.getResultList();
            tq1 = entityManager.createNamedQuery("ForumCategory.getCat", ForumCategory.class);
            tq1.setParameter("sid", user);
            tempList.addAll(tq1.getResultList());
        }else if(user instanceof Admin){
            TypedQuery<ForumCategory> tq1 = entityManager.createNamedQuery("ForumCategory._getCategories", ForumCategory.class);
            tempList = tq1.getResultList();
        }

        List<String> categoryName = new ArrayList();
        for(ForumCategory x : tempList){
            categoryName.add(x.getName());
        }
        forumObservableList.setAll(categoryName);
        eventDiscussion.setItems(forumObservableList);
        eventDiscussion.getSelectionModel().selectFirst();

        eventDiscussion.valueProperty().addListener((observable, oldValue, newValue) -> view.getChildren().remove(threadListView));

        initThreadListView();
        view.getChildren().addListener((ListChangeListener.Change<?extends Node > change) -> {
            while(change.next()){
                if(change.wasRemoved()){
                    if (change.getList().isEmpty()) {
                        initThreadListView();
                    }
                }
            }
        });
    }

    @FXML
    private void initAddTopic() throws IOException {
        addTopicBtn.setDisable(true);
        TypedQuery<ForumCategory> tq2 = entityManager.createNamedQuery("ForumCategory.getCategoryByName", ForumCategory.class);
        tq2.setParameter("fname", eventDiscussion.getSelectionModel().getSelectedItem());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/postTopic.fxml"));
        addTopicController ATC = new addTopicController(tq2.getSingleResult());
        loader.setController(ATC);
        AnchorPane addTopicFXML = loader.load();

        view.getChildren().add(addTopicFXML);
        view.getChildren().remove(threadListView);
    }
}
