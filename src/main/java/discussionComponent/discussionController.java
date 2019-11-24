package discussionComponent;

import authentification.CurrentAccountSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.Account;
import models.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class discussionController implements Initializable {
    @FXML
    private Pane view;

    @FXML
    private Button addTopicBtn;

    @FXML
    private ChoiceBox eventDiscussion;

    @FXML
    private Label postingEventTo;

    // ------------------------------------------------------------------------
    // TEST
    // ------------------------------------------------------------------------
    ObservableList observableList = FXCollections.observableArrayList();
    ObservableList forumObservableList = FXCollections.observableArrayList();
    private ListView threadListView;
    public static List<ThreadListObject> threadListElementSet = new ArrayList<>();
    private Account user = CurrentAccountSingleton.getInstance().getAccount();

    private void setListView(){
        threadListView.getItems().clear();
        observableList.setAll(threadListElementSet);
        threadListView.setItems(observableList);
        threadListView.setCellFactory((Callback<ListView<ThreadListObject>, ListCell<ThreadListObject>>) param -> new ThreadListViewCell());
    }

    private void initThreadListView(){
        threadListView = new ListView();
        threadListView.setMinWidth(600.0);
        setListView();
        if(addTopicBtn.isDisabled()) addTopicBtn.setDisable(false);
        view.getChildren().add(threadListView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forumObservableList.add("General");
        forumObservableList.add(((User)user).getCourse().getName());
        eventDiscussion.setItems(forumObservableList);
        eventDiscussion.valueProperty().setValue("General");
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/postTopic.fxml"));
        addTopicController ATC = new addTopicController(eventDiscussion.getValue().toString());
        loader.setController(ATC);
        AnchorPane addTopicFXML = loader.load();

        view.getChildren().add(addTopicFXML);
        view.getChildren().remove(threadListView);
    }
}
