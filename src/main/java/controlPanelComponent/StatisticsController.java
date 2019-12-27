package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import com.jfoenix.controls.JFXTextArea;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mainUI.MainPane;
import models.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Class which is responsible for controlling the Statistics tab.
 *
 * @author Aleksejs Marmiss
 */
@SuppressWarnings("JpaQueryApiInspection")
public class StatisticsController {
    @FXML
    private Label nrBookReject;
    @FXML
    private Label nrBookCanc;
    @FXML
    private Label nrBookPend;
    @FXML
    private StackPane stackpane;
    @FXML
    private Label averageRating;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button reply;
    @FXML
    private Label nrOfUsers;
    @FXML
    private Label nrOfBookingsPerEvent;
    @FXML
    private Label pastEvents;
    @FXML
    private Label nrOfEvents;
    @FXML
    private Label nrOfBookingsPerUser;
    @FXML
    private Label moneySpent;
    @FXML
    private Label nrBookVal;
    @FXML
    private Label nrOver;
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private LineChart<?,?> linechart;
    @FXML
    private Pagination feedbacks;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private List<Transactions> transactionsList;
    private List<User> usersList;
    private List<Events> eventsList;
    private List<Feedback> feedbackList;

    /**
     * Method that initializes the Statisticcs tab.
     * @param eventsList List of events taken from the database.
     * @param usersList List of users taken from the database.
     * @param transactionsList List of transactions taken from the database.
     */
    public void initialize(List<Events> eventsList, List<User> usersList,List<Transactions> transactionsList) {

        this.eventsList = eventsList;
        this.usersList = usersList;
        this.transactionsList = transactionsList;
        calculateStatistics();
    }

    /**
     * Method that loads the feedback from database.
     */
    private void loadFeedbacks(){
        EntityManager entityManager = admin.getConnection();
        TypedQuery<Feedback> feedbackQuery;
        feedbackQuery = entityManager.createNamedQuery(
                "Feedback.findAllFeedbacks",
                Feedback.class);
        if(!HandleNet.hasNetConnection()){
            try {
                throw new Exception("Internet Connection lost");
            }catch(Exception exc){
                Convenience.showAlert(Alert.AlertType.WARNING, "Ooops", "Something went wrong.", "Please try again later");
            }
        }
        feedbackList = new ArrayList<>(feedbackQuery.getResultList());
    }

    /**
     * Method which populates a page in pagination.
     * @param pageIndex index of the page which has to be populated.
     * @return VBox with content.
     */
    private VBox createPage(int pageIndex){
        VBox pageBox = new VBox();
        JFXTextArea messageContent = new JFXTextArea();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(940);
        messageContent.setMinHeight(100);
        messageContent.setMaxHeight(100);
        messageContent.setEditable(false);
        messageContent.setStyle("-fx-text-fill:  #32a4ba; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Calisto MT Bold; -fx-font-style: Italic");
        int size = feedbackList.size();
        if(size < 1) {
            feedbacks.setPageCount(1);
        } else {
            feedbacks.setPageCount(feedbackList.size());
        }
        try {
            messageContent.setText(
                            "From: " + feedbackList.get(pageIndex).getUserID().getFirstname()
                            + " " + feedbackList.get(pageIndex).getUserID().getLastname() + "\n" +
                            "Rating: " + feedbackList.get(pageIndex).getRatingScore() + "\n" + "\n" +
                            feedbackList.get(pageIndex).getRatingDescription());
        }catch (IndexOutOfBoundsException ioe){
            messageContent.setText("No feedbacks at the moment....");
        }
        messageContent.setFont(Font.font("Serif", FontWeight.BOLD, 16));

        pageBox.getChildren().add(messageContent);
        return pageBox;
    }

    /**
     * Method which opens the homepage.
     * @param mouseEvent Mouse event triggered by the click of the button.
     */
    public void goHome(MouseEvent mouseEvent) {
        try{
            Convenience.switchScene(mouseEvent, getClass().getResource("/FXML/mainUI.fxml"));
        }catch(Exception ex){
            Convenience.showAlert(Alert.AlertType.WARNING, "Ooops", "Something went wrong.", "Please try again later");
        }
    }

    public void calculateStatistics(){
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < transactionsList.size(); i++) {
            series.getData().add(new XYChart.Data(transactionsList.get(i).getDate().toString(), i));
        }
        linechart.getData().clear();
        linechart.getData().addAll(series);
        Calendar todayDate = Calendar.getInstance();
        Date date = new Date();
        todayDate.setTime(date);
        List<Transactions> listPend = new ArrayList<>();
        List<Transactions> listOver = new ArrayList<>();
        List<Transactions> listAccept = new ArrayList<>();
        List<Transactions> listCanceled = new ArrayList<>();
        List<Transactions> listRejected = new ArrayList<>();
        for (Transactions transaction : transactionsList
        ) {
            if (transaction.getCompleted() == 0) {
                listPend.add(transaction);
            }else if(transaction.getCompleted() == 1){
                listAccept.add(transaction);
            }else if(transaction.getCompleted() == 3){
                listCanceled.add(transaction);
            }else if(transaction.getCompleted() == 2){
                listRejected.add(transaction);
            }

            Calendar transDate = Calendar.getInstance();
            Calendar eventDate = Calendar.getInstance();
            transDate.setTime(transaction.getDate());
            eventDate.setTime(transaction.getEvent().getDate());
            if (todayDate.after(eventDate) && transaction.getCompleted() != 1) {
                listOver.add(transaction);
            }
        }
        nrBookVal.setText(String.valueOf(listAccept.size()));
        nrBookPend.setText(String.valueOf(listPend.size()));
        nrBookCanc.setText(String.valueOf(listCanceled.size()));
        nrBookReject.setText(String.valueOf(listRejected.size()));
        nrOver.setText(String.valueOf(listOver.size()));
        nrOfUsers.setText(String.valueOf(usersList.size()));
        DecimalFormat df = new DecimalFormat("0.00");
        nrOfBookingsPerUser.setText(df.format(Double.valueOf(transactionsList.size()) / Double.valueOf(usersList.size())));
        double total = 0;
        for (Transactions transaction : transactionsList
        ) {
            if(transaction.getCompleted() == 1) {
                total += transaction.getEvent().getPrice();
            }
        }
        moneySpent.setText(df.format(total / usersList.size()) + " €");
        nrOfEvents.setText(String.valueOf(eventsList.size()));
        nrOfBookingsPerEvent.setText(df.format(transactionsList.size() / eventsList.size()));
        List<Events> pastEventsList = new ArrayList<>();
        for (Events event : eventsList
        ) {
            Calendar eventDate = Calendar.getInstance();
            eventDate.setTime(event.getDate());
            if (todayDate.after(eventDate)) {
                pastEventsList.add(event);
            }
        }
        pastEvents.setText(String.valueOf(pastEventsList.size()));
        loadFeedbacks();
        double average = 0;
        for (Feedback feedback : feedbackList) {
            average += feedback.getRatingScore();
        }
        average = average/feedbackList.size();
        averageRating.setText(String.valueOf(average));
        feedbacks.setPageFactory(this::createPage);
    }

}




