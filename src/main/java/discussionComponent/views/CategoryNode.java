package discussionComponent.views;

import authentification.CurrentAccountSingleton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.ForumCategory;

import javax.persistence.EntityManager;
import java.io.IOException;
import javafx.fxml.FXML;

public class CategoryNode {
    @FXML private AnchorPane categoryNode;
    @FXML private Label categoryName;

    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private ForumCategory category;

    public CategoryNode(ForumCategory category){
        this.category = category;

    }

    @FXML
    private void viewCategory() throws IOException{

    }

    public AnchorPane getCategoryNode() { return categoryNode; }

}
