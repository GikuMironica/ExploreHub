package discussionComponent.controllers;

import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Account;
import models.Admin;
import models.Post;
import models.Topic;

import javafx.fxml.FXML;
import javax.persistence.EntityManager;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class NewPostController implements Initializable {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private Account account = CurrentAccountSingleton.getInstance().getAccount();
    private Topic topic;
    @FXML
    Text postModeText;
    @FXML
    JFXTextField topicTitleField;
    @FXML
    JFXTextArea postMessageArea;
    @FXML
    JFXCheckBox lockTopicCheckBox, announcementCheckBox;
    @FXML
    JFXButton submitButton, cancelButton;

    public NewPostController(Topic topic){
        this.topic = topic;
    }

    public void initialize(URL url, ResourceBundle rb){
        postModeText.setText("Posting to "+ topic.getThreadTitle());
        lockTopicCheckBox.setVisible(false);
        announcementCheckBox.setVisible(false);
        ((VBox) topicTitleField.getParent()).getChildren().remove(1);

    }

    @FXML void submit(){
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
        Post newPost = new Post(account, postMessageArea.getText(), timestamp);
        newPost.setTopic(topic);

        em.getTransaction().begin();
        em.persist(newPost);
        topic.setThreadLastPost(newPost);
        em.merge(topic);
        em.getTransaction().commit();

        cancel();
    }

    @FXML void cancel(){
        Scene scene = cancelButton.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(2)).setVisible(true);
        contentController.getChildren().remove(3);
    }
}
