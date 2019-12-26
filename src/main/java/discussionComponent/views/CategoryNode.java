package discussionComponent.views;

import authentification.CurrentAccountSingleton;
import discussionComponent.controllers.DisplayCategoryController;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ForumCategory;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

public class CategoryNode {
    @FXML private AnchorPane categoryNode;
    @FXML private Text categoryTitle;

    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private ForumCategory category;
    private Double listViewWidth;

    public CategoryNode(ForumCategory category, ReadOnlyDoubleProperty listViewWidth){
        this.category = category;
        this.listViewWidth = listViewWidth.doubleValue();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/discussion/categoryNode.fxml"));
        loader.setController(this);
        try {
            categoryNode = loader.load();
            categoryNode.setPrefWidth(this.listViewWidth);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    void setElement(){
        categoryTitle.setText(category.getName());
    }

    @FXML
    private void viewCategory() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        DisplayCategoryController displayCategoryController = new DisplayCategoryController(category);
        loader.setController(displayCategoryController);

        Scene scene = categoryNode.getScene();
        AnchorPane contentController = (AnchorPane) scene.lookup("#contentPane");
        contentController.getChildren().remove(0);
        // VBox categoryTopicDisplay = loader.load();
        // contentController.getChildren().add(categoryTopicDisplay);
        System.out.println(category.getName());
    }

    public AnchorPane getCategoryNode() { return categoryNode; }

}
