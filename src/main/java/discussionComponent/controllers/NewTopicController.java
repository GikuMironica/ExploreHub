package discussionComponent.controllers;

import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.*;

import javax.persistence.EntityManager;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class NewTopicController implements Initializable {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private Account account = CurrentAccountSingleton.getInstance().getAccount();
    private ForumCategory forumCategory;
    @FXML Text postModeText;
    @FXML JFXTextField topicTitleField;
    @FXML JFXTextArea postMessageArea;
    @FXML JFXCheckBox lockTopicCheckBox, announcementCheckBox;
    @FXML JFXButton submitButton, cancelButton;

    public NewTopicController(ForumCategory forumCategory){
        this.forumCategory = forumCategory;
    }

    public void initialize(URL url, ResourceBundle rb){
        postModeText.setText("Creating new topic in " + forumCategory.getName());
        if(account instanceof Admin){
            // Do nothing.
        }else{
            lockTopicCheckBox.setVisible(false);
            announcementCheckBox.setVisible(false);
        }
    }

    @FXML void cancel(){
        Scene scene = cancelButton.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        ((Node) contentController.getChildren().get(1)).setVisible(true);
        contentController.getChildren().remove(2);
    }

    @FXML void submit(){
        if(postMessageArea.getText().length() > 5){
            Post newPost = new Post(account, postMessageArea.getText(), String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()));
            Topic newTopic = new Topic(forumCategory, topicTitleField.getText(), account, 0, 0);

            em.getTransaction().begin();
            em.persist(newTopic);
            newPost.setTopic(newTopic);

            em.persist(newPost);

            newTopic.setThreadFirstPost(newPost);
            newTopic.setThreadLastPost(newPost);

            em.merge(newTopic);
            em.getTransaction().commit();

            cancel();
        }
    }
}
