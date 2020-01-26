package discussionComponent.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import models.ForumCategory;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTopicController implements Initializable {
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
        postModeText.setText(forumCategory.getName());
    }
}
