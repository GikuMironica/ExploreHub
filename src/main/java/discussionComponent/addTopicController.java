package discussionComponent;

import authentification.CurrentAccountSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.Account;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class addTopicController implements Initializable {
    @FXML
    private Label postingEventTo;

    @FXML
    private TextField topic;

    @FXML
    private TextArea message;

    @FXML
    private AnchorPane createTopicAP;

    private String forumName;
    private Account user = CurrentAccountSingleton.getInstance().getAccount();

    public addTopicController(String forumName){
        this.forumName = forumName;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postingEventTo.setText("Posting Topic to " + this.forumName);

    }

    @FXML void postTopic(MouseEvent mouseEvent){
        if(message.getText().length() > 10){
            discussionController.threadListElementSet.add(new ThreadListObject(10, topic.getText(), user.getFirstname(), "24.11.2019"));
            ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
        }
    }
    @FXML
    private void cancel(MouseEvent mouseEvent){
        ((Pane) createTopicAP.getParent()).getChildren().remove(createTopicAP);
    }


}
