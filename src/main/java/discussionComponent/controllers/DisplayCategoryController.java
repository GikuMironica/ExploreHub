package discussionComponent.controllers;

import authentification.CurrentAccountSingleton;
import javafx.fxml.Initializable;
import models.ForumCategory;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.ResourceBundle;

public class DisplayCategoryController implements Initializable {
    private ForumCategory category;

    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();

    public DisplayCategoryController(ForumCategory category){
        this.category = category;
    }

    public void initialize(URL url, ResourceBundle rb){

    }
}
