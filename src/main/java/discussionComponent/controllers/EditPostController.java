package discussionComponent.controllers;

import javafx.fxml.Initializable;
import models.Post;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPostController implements Initializable {
    private Post post;

    public EditPostController(Post p){
        this.post = p;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

}
