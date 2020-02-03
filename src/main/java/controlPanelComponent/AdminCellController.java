package controlPanelComponent;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.Admin;

public class AdminCellController extends JFXListCell<Admin> {

    @FXML
    private Label idLabel, emailLabel, firstnameLabel, lastnameLabel;
    private FXMLLoader loader;
    @FXML
    private AnchorPane admincellPane;

    @Override
    protected synchronized void updateItem(Admin admin, boolean empty) {
        super.updateItem(admin, empty);

        if (empty || admin == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/FXML/adminCell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            initialiseViews(admin);
            setText(null);
            setGraphic(admincellPane);

        }
    }

    private void initialiseViews(Admin admin) {
        idLabel.setText(String.valueOf(admin.getId()));
        emailLabel.setText(admin.getEmail());
        firstnameLabel.setText(admin.getFirstname());
        lastnameLabel.setText(admin.getLastname());

    }
}
