package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import authentification.MessageHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.sun.javafx.scene.shape.LineHelper;
import handlers.Convenience;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Account;
import models.Events;
import models.Transactions;
import models.User;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class StatisticsController {

    public StackPane stackpane;
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
    private Label nrBook;
    @FXML
    private Label nrPend;
    @FXML
    private Label nrOver;
    @FXML
    private NumberAxis y;
    @FXML
    private CategoryAxis x;
    @FXML
    private LineChart<?,?> linechart;

    @FXML
    private Pagination mails;
    private Message[] messages;
    private final Account admin = CurrentAccountSingleton.getInstance().getAccount();
    private List<Transactions> transactionsList;
    private List<User> usersList;
    private List<Events> eventsList;



    public void initialize(List<Events> eventsList, List<User> usersList,List<Transactions> transactionsList) {

        this.eventsList = eventsList;
        this.usersList = usersList;
        this.transactionsList = transactionsList;
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < transactionsList.size(); i++) {
            series.getData().add(new XYChart.Data(transactionsList.get(i).getDate().toString(), i));
        }
        linechart.getData().addAll(series);
        nrBook.setText(String.valueOf(transactionsList.size()));
        List<Transactions> listPend = new ArrayList<>();
        List<Transactions> listOver = new ArrayList<>();
        for (Transactions transaction : transactionsList
        ) {
            if (transaction.getCompleted() == 0) {
                listPend.add(transaction);
            }
            Calendar transDate = Calendar.getInstance();
            Calendar eventDate = Calendar.getInstance();
            transDate.setTime(transaction.getDate());
            eventDate.setTime(transaction.getEvent().getDate());
            if (transDate.after(eventDate)) {
                listOver.add(transaction);
            }
        }
        nrPend.setText(String.valueOf(listPend.size()));
        nrOver.setText(String.valueOf(listOver.size()));
        nrOfUsers.setText(String.valueOf(usersList.size()));
        DecimalFormat df = new DecimalFormat("0.00");
        nrOfBookingsPerUser.setText(df.format(Double.valueOf(transactionsList.size()) / Double.valueOf(usersList.size())));
        double total = 0;
        for (Events event : eventsList
        ) {
            total += event.getPrice();
        }
        moneySpent.setText(df.format(total / usersList.size()));
        nrOfEvents.setText(String.valueOf(eventsList.size()));
        nrOfBookingsPerEvent.setText(df.format(transactionsList.size() / eventsList.size()));
        Calendar todayDate = Calendar.getInstance();
        Date date = new Date();
        todayDate.setTime(date);
        List<Events> pastEventsList = new ArrayList<>();
        for (Events event : eventsList
        ) {
            Calendar eventDate = Calendar.getInstance();
            eventDate.setTime(event.getDate());
            if (eventDate.after(todayDate)) {
                pastEventsList.add(event);
            }
        }
        pastEvents.setText(String.valueOf(pastEventsList.size()));
        check();


    }


    public void openHomepage(MouseEvent mouseEvent) throws IOException {
        Convenience.switchScene(mouseEvent,getClass().getResource("/FXML/mainUI.fxml"));
    }

    public void check()
    {
        try {
            String username = "iexplore.confirmation@gmail.com";// change accordingly
            String password = "cts5-2019";// change accordingly
            String host = "IMAP.gmail.com";// change accordingly
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "993");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);
            Folder emailFolder = store.getFolder("Inbox");
            emailFolder.open(Folder.READ_ONLY);
            messages = emailFolder.getMessages();
            mails.setPageFactory(this::createPage);
           // emailFolder.close(false);
           // store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createPage(int pageIndex){


        VBox pageBox = new VBox();
        TextArea messageContent = new TextArea();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(940);
        messageContent.setEditable(false);
        try {
            mails.setPageCount(messages.length);
            Message message = messages[(messages.length-1)-pageIndex];
            messageContent.setText("FROM: " + Arrays.toString(messages[(messages.length-1)-pageIndex].getFrom()) + "\n" +
                    "SUBJECT: " + messages[(messages.length-1)-pageIndex].getSubject() + "\n" +
                    "DATE: " + messages[(messages.length-1)-pageIndex].getSentDate() + "\n" +
                    getTextFromMessage(message) + "\n");

        }catch (IOException ioe){
            //
        }catch (MessagingException me){
            ///
        }

        pageBox.getChildren().add(messageContent);

        return pageBox;
    }


    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }


    private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart){
            result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            // alternatives appear in an order of increasing
            // faithfulness to the original content. Customize as req'd.
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    public void replyButton(MouseEvent mouseEvent) {
        int mailSelected = (mails.getPageCount()-1) - mails.currentPageIndexProperty().intValue();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Your message"));
        TextArea textArea = new TextArea();
        textArea.setPromptText("Please input your message....");
        content.setBody(textArea);
        JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Send");
        button.setOnAction(actionEvent -> {
            try {
                Address[] replyTo = messages[mailSelected].getFrom();
                String subject = messages[mailSelected].getSubject();
                MessageHandler messageHandler = MessageHandler.getMessageHandler();
                String email = replyTo == null ? null : ((InternetAddress) replyTo[0]).getAddress();
                messageHandler.sendEmail(textArea.getText(), subject, email);
                dialog.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        content.setActions(button);
        dialog.show();


    }

//    public void loadUserStatistics(){
//        EntityManager entityManager = admin.getConnection();
//        TypedQuery<User> usersQuery;
//        usersQuery = entityManager.createNamedQuery(
//                "User.findAllUser",
//                User.class);
//        usersList = new ArrayList<>(usersQuery.getResultList());
//
//    }
//
//    public void loadTransactionStatistics(){
//        EntityManager entityManager = admin.getConnection();
//        TypedQuery<Transactions> transactionsQuery;
//        transactionsQuery = entityManager.createNamedQuery(
//                "Transactions.findAllTransactions",
//                Transactions.class);
//        transactionsList = new ArrayList<>(transactionsQuery.getResultList());
//
//    }
//
//    public void loadEventStatistics(){
//        EntityManager entityManager = admin.getConnection();
//        TypedQuery<Events> eventsQuery;
//        eventsQuery = entityManager.createNamedQuery(
//                "Events.findAllEvents",
//                Events.class);
//        eventsList = new ArrayList<>(eventsQuery.getResultList());
//
//    }

}




