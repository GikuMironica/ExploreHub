package navbarComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import controlPanelComponent.PreLoader;
import filterComponent.FilterSingleton;
import handlers.Convenience;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import listComponent.EventListSingleton;
import models.Account;
import models.Admin;
import sidebarComponent.SidebarController;
import sidebarComponent.SidebarState;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class which controls the navigation bar (toolbar)
 * @author Hidayat Rzayev
 */
public class  NavbarController implements Initializable {

    @FXML
    private AnchorPane navbarPane;

    @FXML
    private ImageView panelImageView;

    @FXML
    private ImageView refreshImageView;

    @FXML
    private ImageView searchImageView;

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private SidebarController sidebarController;

    @FXML
    private JFXHamburger menuHamburger;

    private HamburgerSlideCloseTransition menuCloseTransition;
    private FilterSingleton filter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPanelImageView();
        initSearchTextField();
        initMenuButton();
        initSidebar();

        filter = FilterSingleton.getInstance();
    }

    private void initPanelImageView() {
        Account currentAccount = CurrentAccountSingleton.getInstance().getAccount();
        if (currentAccount instanceof Admin) {
            panelImageView.setVisible(true);
        }
    }

    private void initSearchTextField() {
        searchTextField.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                showSearchTextField();
            } else {
                if (searchTextField.getText().isBlank()) {
                    hideSearchTextField();
                }
            }
        });
    }

    private void initMenuButton() {
        menuCloseTransition = new HamburgerSlideCloseTransition(menuHamburger);
        menuCloseTransition.setRate(-1);
    }

    private void initSidebar() {
        boolean isSidebarHidden = SidebarState.getStateHidden();
        if (!isSidebarHidden) {
            sidebarController.show();
            menuCloseTransition.setRate(1);
            menuCloseTransition.play();
        }
    }

    /**
     * Loads the homepage
     *
     * @param mouseEvent - the even which triggered the method
     */
    @FXML
    private void handleTitleClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
     * Opens the admin panel
     *
     * @param mouseEvent - the even which triggered the method
     */
    @FXML
    private void handlePanelClicked(MouseEvent mouseEvent) throws IOException{
        //Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/PreLoader.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/PreLoader.fxml"));
        Parent root = (Parent) loader.load();
        PreLoader controller = (PreLoader) loader.getController();
        controller.setScene(((Node) mouseEvent.getSource()).getScene());
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        Stage window = new Stage();
        window.setScene(scene);
        window.initStyle(StageStyle.UNDECORATED);
        window.setTitle("Loading");
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
       // stage.hide();
    }

    /**
     * Loads the homepage
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleHomeClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }

    /**
<<<<<<< HEAD
     * If the sidebar is hidden, then it will be shown. Otherwise it will be hidden.
=======
     * Loads the Discussion page
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleDiscussionClicked(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/discussionView.fxml"));
        } catch (IOException ioe) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
            ioe.printStackTrace();
        }
    }

    /**
     * Opens the sidebar
>>>>>>> [DiscussionComponent] UI implemented
     *
     * @param mouseEvent - the event which triggered the method
     */
    @FXML
    private void handleMenuClicked(MouseEvent mouseEvent) {
        menuCloseTransition.setRate(menuCloseTransition.getRate() * -1);
        menuCloseTransition.play();

        if (sidebarController.isHidden()) {
            sidebarController.show();
        } else {
            sidebarController.hide();
        }

        SidebarState.saveStateHidden(sidebarController.isHidden());
    }

    @FXML
    private void handleRefreshClicked(MouseEvent mouseEvent){
        EventListSingleton eventList = EventListSingleton.getInstance();
        eventList.refreshList();

        refreshImageView.setDisable(true);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(5)
        );

        visiblePause.setOnFinished(
                (ActionEvent ev) -> {
                    refreshImageView.setDisable(false);
                }
        );
        visiblePause.play();
    }

    @FXML
    private void handleSearchEntered(MouseEvent mouseEvent) {
        showSearchTextField();
    }

    @FXML
    private void handleSearchExited(MouseEvent mouseEvent) {
        if (searchTextField.getText().isBlank() && !searchTextField.isFocused()) {
            hideSearchTextField();
        }
    }

    @FXML
    private void handleSearchTextFieldEntered(MouseEvent mouseEvent) {
        showSearchTextField();
    }

    @FXML
    private void handleSearchTextFieldExited(MouseEvent mouseEvent) {
        if (searchTextField.getText().isBlank() && !searchTextField.isFocused()) {
            hideSearchTextField();
        }
    }

    @FXML
    private void handleSearchTextFieldTyped(KeyEvent keyEvent) {
        filter.setSearchKeyword(searchTextField.getText());
        filter.filterItems();
    }

    private void showSearchTextField() {
        animateSearchField(90, 190);
    }

    private void hideSearchTextField() {
        animateSearchField(0, 0);
        searchTextField.clear();
        navbarPane.requestFocus();
    }

    private void animateSearchField(double searchIconAngle, double textFieldWidth) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(searchTextField.prefWidthProperty(), searchTextField.getWidth()),
                        new KeyValue(searchImageView.rotateProperty(), searchImageView.getRotate())),
                new KeyFrame(Duration.millis(250),
                        new KeyValue(searchTextField.prefWidthProperty(), textFieldWidth),
                        new KeyValue(searchImageView.rotateProperty(), searchIconAngle)));
        timeline.play();
    }
}
