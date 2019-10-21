package sample;

import Models.Users;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;

public class Main extends Application {

    private Stage window;
    private Scene scene;
    private TextField Id, Name, Email, Access;
    private TableView<Users> table;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");


        window = primaryStage;
        window.setTitle("TableView");

        TableColumn<Users, Integer> IdColumn = new TableColumn<>("Id");
        IdColumn.setMinWidth(100);
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<Users, String> NameColumn = new TableColumn<>("Name");
        NameColumn.setMinWidth(100);
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TableColumn<Users, String> EmailColumn = new TableColumn<>("Email");
        EmailColumn.setMinWidth(150);
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
        TableColumn<Users, Integer> AccessColumn = new TableColumn<>("Access");
        AccessColumn.setMinWidth(100);
        AccessColumn.setCellValueFactory(new PropertyValueFactory<>("Access"));

        Email = new TextField();
        Email.setPromptText("Email");
        Email.setMinWidth(100);

        Name = new TextField();
        Name.setPromptText("Name");
        Name.setMinWidth(100);

        Access = new TextField();
        Access.setPromptText("Access");
        Access.setMinWidth(100);

        Button addButton = new Button("Add User");
        addButton.setOnAction(e-> addButtonClicked());

        Button deleteButton = new Button("Delte User");
        deleteButton.setOnAction(e-> deleteButtonClicked());

        HBox hBox = new HBox();

        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10); 	// inputs are separated from form with10 pixels
        hBox.getChildren().addAll(Name, Email, Access, addButton, deleteButton);

        table = new TableView<>();
        table.setItems(getProduct());
        table.getColumns().addAll(IdColumn, NameColumn, EmailColumn, AccessColumn);


        // Layout
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        // test user connection
        // UserConnectionSingleton user = UserConnectionSingleton.getInstance();
        // Query q1 = user.getManager().createNativeQuery("INSERT INTO lol(Name) VALUES('haha')");
        // List<String> l3 = q1.getResultList();     --- ERROR HERE, NOT ENOUGH PRIVILEGES
        // Query q2 = user.getManager().createNativeQuery("SELECT * FROM lol");
        // List<String> l2 = q2.getResultList();

        scene = new Scene(vBox);
        window.setScene(scene);
        window.show();

    }

    private ObservableList<Users> getProduct() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        UserConnectionSingleton user = UserConnectionSingleton.getInstance();

        TypedQuery<Users> tq1 = user.getManager().createNamedQuery(
                "Users.SelectUsers",                                        // ignore
                Users.class);
        List<Users> l1 = tq1.getResultList();
        Iterator<Users> it = l1.iterator();
        while(it.hasNext()){
            users.add(it.next());
        }
        return users;
    }

    private void deleteButtonClicked() {
        Users u1 = table.getSelectionModel().getSelectedItem();
        int id = u1.getId();
        AdminConnectionSingleton admin = AdminConnectionSingleton.getInstance();
        Users u2 = admin.getManager().find(Users.class, id);
        admin.getManager().getTransaction().begin();
        admin.getManager().remove(u2);
        admin.getManager().getTransaction().commit();
        ObservableList<Users> allUsers = table.getItems();
        allUsers.remove(u1);
    }

    private void addButtonClicked() {
        Users u1 = new Users();
            // try catch check format of input data
        u1.setName(Name.getText());
        u1.setEmail(Email.getText());
        u1.setAccess(Integer.valueOf(Access.getText()));
        AdminConnectionSingleton admin = AdminConnectionSingleton.getInstance();
        admin.getManager().getTransaction().begin();
        admin.getManager().persist(u1);
        admin.getManager().getTransaction().commit();
        table.getItems().add(u1);
        Email.clear();
        Name.clear();
        Access.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
