package discussionComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.Account;
import models.Post;
import models.Thread;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class threadViewController implements Initializable {

    private ObservableList observableList = FXCollections.observableArrayList();
    private static List<Post> postListElementSet = new ArrayList<>();

    private Account user = CurrentAccountSingleton.getInstance().getAccount();
    private static EntityManager entityManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entityManager = user.getConnection();
        forumName.setText(fName);
        threadTitle.setText(tTitle);
        threadAuthor.setText(tAuthor);
        initPostListView();

        threadViewAP.visibleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(true)){
                initPostListView();
            }
    }));

    }

    private int threadId;
    private String fName;
    private String tTitle;
    private String tAuthor;
    private Thread thread;

    @FXML
    private AnchorPane threadViewAP;
    @FXML
    private Label forumName;

    @FXML
    private VBox postDisplayVbox;

    @FXML
    private Label threadAuthor;

    @FXML
    private Label threadTitle;

    @FXML
    private ListView threadListView;

    @FXML
    private void postReply() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/postReply.fxml"));
        postReplyController ATC = new postReplyController(thread);
        loader.setController(ATC);
        AnchorPane postReplyFXML = loader.load();
        ((Pane) threadViewAP.getParent()).getChildren().add(postReplyFXML);
        apHide();
    }

    private void apHide(){
        threadViewAP.setVisible(false);
    }

    public void apShow(){
        initPostListView();
        threadViewAP.setVisible(true);
    }

    @FXML
    private void goBack(){
        ((Pane) threadViewAP.getParent()).getChildren().remove(threadViewAP);
    }

    private void initPostListView(){
        threadListView = new ListView();
        TypedQuery<Post> tq1 = entityManager.createNamedQuery("Post.getPostbyThread", Post.class);
        tq1.setParameter("t", thread);
        postListElementSet = tq1.getResultList();
        setListView(postListElementSet);
        postDisplayVbox.getChildren().remove(2);
        postDisplayVbox.getChildren().add(2, threadListView);
    }

    private void setListView(List<Post> postListElementSet){
        threadListView.getItems().clear();
        observableList.setAll(postListElementSet);
        threadListView.setItems(observableList);
        threadListView.setCellFactory((Callback<ListView<Post>, ListCell<Post>>) param -> new PostListViewCell());
    }


    public threadViewController(Thread tlo){
        this.thread = tlo;
        this.threadId = tlo.getId();
        this.fName = tlo.getCategory().getName();
        this.tTitle = tlo.getThreadTitle();
        this.tAuthor = tlo.getThreadAuthor().getFirstname();
    }
}
