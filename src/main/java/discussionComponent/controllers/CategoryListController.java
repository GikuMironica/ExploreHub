package discussionComponent.controllers;

import authentification.CurrentAccountSingleton;
import discussionComponent.views.CategoryListViewCell;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.ForumCategory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javafx.util.Callback;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryListController implements Initializable {
    private EntityManager em = CurrentAccountSingleton.getInstance().getAccount().getConnection();
    private AnchorPane parentPane;

    private List<ForumCategory> categoryList;
    private ObservableList catObservableList;

    @FXML VBox catVbox;

    public CategoryListController(AnchorPane parentPane){
        this.parentPane = parentPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        TypedQuery<String> t1 = em.createNamedQuery("ForumCategory.getCategoryType", String.class);
        List<String> result = t1.getResultList();
        TypedQuery<ForumCategory> t2 = em.createNamedQuery("ForumCategory.getCategoryByType", ForumCategory.class);
        Platform.runLater(() ->{
            for (String type : result){
                catVbox.getChildren().add(new Text(type));
                t2.setParameter("fType", type);
                categoryList = t2.getResultList();
                catObservableList = FXCollections.observableArrayList();
                catObservableList.setAll(categoryList);
                ListView<ForumCategory> categoryListView = new ListView<>();
                categoryListView.setItems(catObservableList);
                categoryListView.setCellFactory(param ->new CategoryListViewCell(categoryListView));
                categoryListView.prefHeightProperty().bind(Bindings.size(catObservableList).multiply(106));
                categoryListView.getStylesheets().add("/Styles/postList.css");
                catVbox.getChildren().add(categoryListView);



            }
        });
        Platform.runLater(()-> catVbox.getScene().getWindow().widthProperty().addListener((observable, oldValue, newValue) -> {
            for(Node n : catVbox.getChildren()){
                if(n instanceof ListView){
                    ((ListView) n).refresh();
                }
            }
        }));



    }
}
