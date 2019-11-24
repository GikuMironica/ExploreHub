package discussionComponent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ThreadListElement {
    @FXML
    private AnchorPane listElement;
    @FXML
    private Label threadTitle;
    @FXML
    private Label threadStartedOn;
    @FXML
    private Label threadResponseCount;
    @FXML
    private Label threadAuthor;
    @FXML
    private Label threadLastReplyDate;
    @FXML
    private Label threadLastReplyAuthor;
    @FXML
    private Button threadLastReplyBtn;

    public ThreadListElement(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/threadListObject.fxml"));
        fxmlLoader.setController(this);
        try{
            listElement = fxmlLoader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setElement(ThreadListObject tlo){
        threadTitle.setText(tlo.getThreadTitle());
        threadLastReplyAuthor.setText(tlo.getThreadLastResponseAuthor());
        threadResponseCount.setText(tlo.getThreadResponseCount() + " Responses");
        threadLastReplyDate.setText(tlo.getThreadLastResponseDate());
        threadStartedOn.setText("Started by " + tlo.getThreadAuthor() + ", " +tlo.getThreadStartedOn());
    }

    @FXML
    private void openPost(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/postObject.xml"));
    }

    public AnchorPane getListElement(){
        return listElement;
    }
}
