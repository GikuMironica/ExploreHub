package controlPanelComponent;

import alerts.CustomAlertType;
import authentification.loginProcess.CurrentAccountSingleton;
import com.jfoenix.controls.*;
import handlers.HandleNet;
import handlers.MessageHandler;
import handlers.Convenience;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mainUI.MainPane;
import models.Account;


public class CommunicationTabController {
    @FXML
    private JFXButton moveToFolder;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXComboBox folder;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField surname;
    @FXML
    private JFXTextField email;
    @FXML
    private StackPane stackpane;
    @FXML
    private JFXButton sendButton;
    @FXML
    private Pagination mails;
    @FXML
    private Message[] messages;
    private VBox pageBox;
    private VBox backup = new VBox();
    Account account = CurrentAccountSingleton.getInstance().getAccount();
    private  Properties properties;
    private String username = "explorehub.help@gmail.com";
    private String password = "cts5-2019";
    private String host = "IMAP.gmail.com";
    private String currentFolder;


    public void initialize() throws Exception{
        folder.setItems(FXCollections.observableArrayList("Inbox", "Processed"));
        folder.getSelectionModel().select(0);
        currentFolder = folder.getSelectionModel().getSelectedItem().toString();
        checkForEmails();
    }


    /**
     *Method which loads the emails from gmail.
     */
    public void checkForEmails() throws Exception
    {
        String folderName = folder.getSelectionModel().getSelectedItem().toString();
        if(folderName.equals("Inbox")){
            moveToFolder.setText("Move to Processed");
        }else {
            moveToFolder.setText("Move to Inbox");
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception{
            try {
                properties = new Properties();

                properties.put("mail.pop3.host", host);
                properties.put("mail.pop3.port", "993");
                properties.put("mail.pop3.starttls.enable", "true");
                Session emailSession = Session.getDefaultInstance(properties);
                Store store = emailSession.getStore("imaps");
                store.connect(host, username, password);
                Folder emailFolder = store.getFolder(folderName);
                emailFolder.open(Folder.READ_WRITE);
                messages = emailFolder.getMessages();

            } catch (Exception e) {
                throw new Exception("Internet Connection lost");
            }
                return null;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                if (messages.length == 0){
                    mails.setPageCount(1);
                    empty();
                    moveToFolder.setDisable(true);
                }else {
                    create();
                    moveToFolder.setDisable(false);
                }
            }

            @Override
            protected void failed() {
                super.failed();
                connectionFailed();
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    /**
     * Method which binds the emails to the pagination pages.
     */
    public void create(){
        mails.setPageFactory(this::createPage);
    }

    /**
     * Method which called when email folder is emty
     */
    public void empty(){
        mails.setPageFactory(this::createEmptyPage);
    }

    private VBox createEmptyPage(int pageIndex){
        pageBox = new VBox();
        pageBox.alignmentProperty().setValue(Pos.CENTER);
        JFXTextArea messageContent = new JFXTextArea();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(1170);
        messageContent.setMinHeight(300);
        messageContent.setMaxHeight(300);
        messageContent.setEditable(false);
        messageContent.setStyle("-fx-text-fill:  #32a4ba; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Calisto MT Bold; -fx-font-style: Italic");
        messageContent.setText("Folder is empty");
        pageBox.getChildren().clear();
        pageBox.getChildren().add(messageContent);

        return pageBox;
    }


    /**
     * Method which populates a page in pagination.
     * @param pageIndex index of the page which has to be populated.
     * @return VBox with content.
     */
    private VBox createPage(int pageIndex){
        name.clear();
        surname.clear();
        email.clear();

        pageBox = new VBox();
        pageBox.alignmentProperty().setValue(Pos.CENTER);
        JFXTextArea messageContent = new JFXTextArea();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(1170);
        messageContent.setMinHeight(300);
        messageContent.setMaxHeight(300);
        messageContent.setEditable(false);
        messageContent.setStyle("-fx-text-fill:  #32a4ba; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Calisto MT Bold; -fx-font-style: Italic");
        try {
            mails.setPageCount(messages.length);
            Message message = messages[(messages.length-1)-pageIndex];
            String subject = messages[(messages.length-1)-pageIndex].getSubject();
            Pattern p = Pattern.compile("(?<=_)([^_]+)(?=_)");
            Matcher m = p.matcher(subject);
            String from = "unknown";
            if (m.find()){
                from = m.group(1);
                email.setText(from);
            }else {
                from = messages[(messages.length-1)-pageIndex].getFrom()[0].toString();
                Pattern patternFrom = Pattern.compile("(?<=<)([^_]+)(?=>)");
                Matcher matchFrom = patternFrom.matcher(from);
                if (matchFrom.find()){
                    email.setText(matchFrom.group(1));
                }
            }

            String text = "FROM: " + from + "\n" +"\n" +
                    "SUBJECT: " +subject + "\n" +"\n" +
                    "DATE: " + messages[(messages.length-1)-pageIndex].getSentDate() + "\n" + "\n" +
                    getTextFromMessage(message) + "\n";
            messageContent.setText(text);

            if (m.find()){
                String fullName = m.group(1);
                name.setText(fullName.split(" ")[0]);
                surname.setText(fullName.split(" ")[1]);
            }else{
                name.setPromptText("Please enter name");
                surname.setPromptText("Please enter surname");
            }
        }catch (Exception e){
           if (!HandleNet.hasNetConnection()) {
               try {
                   Convenience.popupDialog(MainPane.getInstance().getStackPane(), anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
               } catch (Exception exc) {
                   Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
               }
           }else {
               try {
                   checkForEmails();
               } catch (Exception e1) {
                   Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
               }
           }
        }
        pageBox.getChildren().clear();
        pageBox.getChildren().add(messageContent);
        return pageBox;
    }

    /**
     * Method which extracts the text from message.
     * @param message Message object.
     * @return Message text as String.
     * @throws IOException
     * @throws MessagingException
     */
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

    /***
     * Method which extracts the text from body part of the message.
     * @param bodyPart body part of th message.
     * @return Text from body part of the message as a String.
     * @throws IOException
     * @throws MessagingException
     */
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

    /**
     * Method which extracts text from multzpart of the message.
     * @param mimeMultipart multypart of th message.
     * @return Text from multypart of the message as a String.
     * @throws IOException
     * @throws MessagingException
     */
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {

        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }



    /**
     * Method which handle the button pressed event.
     * @param mouseEvent mouse event which was triggered by a pressed button.
     */
    public void handleReplyClicked(MouseEvent mouseEvent) {
        int mailSelected = (mails.getPageCount()-1) - mails.currentPageIndexProperty().intValue();
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Your message"));
        JFXTextArea textArea = new JFXTextArea();
        textArea.setStyle("-fx-text-fill:  #32a4ba; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: Calisto MT Bold; -fx-font-style: Italic");
        textArea.setMinHeight(250);
        textArea.setMinWidth(500);
        String text = "Dear " + name.getText() + " " + surname.getText() +",\n"
                + "Thank you for your email!\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "Best Regards,\n"
                + account.getFirstname() + " " + account.getLastname() + "\n"
                + "Your ExploreHub Team.";
        textArea.setText(text);
        content.setBody(textArea);
        JFXDialog dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Send");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        button.setStyle("-fx-background-color: #32a4ba");
        button.setOnAction(actionEvent -> {
            if (!HandleNet.hasNetConnection()) {
                    try {
                        dialog.close();
                        Convenience.popupDialog(MainPane.getInstance().getStackPane(), anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
                    } catch (IOException e1) {
                        Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                    }
                }else{
                    try {
                        String subject = messages[mailSelected].getSubject();
                        MessageHandler messageHandler = MessageHandler.getMessageHandler();
                        messageHandler.sendEmail(textArea.getText(), subject, email.getText());
                        dialog.close();
                    } catch (MessagingException e) {
                        Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                    }
                }
        });
        content.setActions(button);
        dialog.show();
    }


    /**
     * Method which opens the homepage.
     * @param mouseEvent Mouse event triggered by the click of the button.
     */
    public void goHome(MouseEvent mouseEvent) {
        try{
            Convenience.openHome();
        }catch(Exception ex){
            Convenience.showAlert(CustomAlertType.WARNING, "Oops, something went wrong. Please, try again later.");
        }
    }

    /**
     * Method that allows to move the message into another folder.
     * @param mouseEvent mouse event
     */
    public void moveToFolder(MouseEvent mouseEvent) {
        moveToFolder.setDisable(true);
        backup.getChildren().addAll(pageBox.getChildren());
        pageBox.getChildren().clear();
        pageBox.getChildren().add(new JFXProgressBar());
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception{
                String folderName = folder.getSelectionModel().getSelectedItem().toString();
                int mailSelected = (mails.getPageCount() - 1) - mails.currentPageIndexProperty().intValue();
                if (folderName.equals("Inbox")) {
                    move("Processed", mailSelected);
                } else {
                    move("Inbox", mailSelected);
                }
                return null;
                }

            @Override
            protected void succeeded(){
                super.succeeded();
                try{
                    checkForEmails();
                }catch(Exception ex){
                    Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                }
            }

            @Override
            protected void failed() {
                super.failed();
                connectionFailed();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Method that allows to move the message into another folder.
     * @param folder folder to which the message should be moved.
     * @param mailSelected email which has to be moved.
     * @throws Exception Exception thrown in case of connection failure.
     */
    private void move(String folder, int mailSelected) throws Exception{
        if (!HandleNet.hasNetConnection()) {
            try {
                Convenience.popupDialog(MainPane.getInstance().getStackPane(), anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
            } catch (IOException e1) {
                Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
            }
        } else {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, username, password);
            Folder emailFolder = store.getFolder(folder);
            emailFolder.open(Folder.READ_WRITE);
            emailFolder.appendMessages(new Message[]{messages[mailSelected]});
            messages[mailSelected].setFlags(new Flags(Flags.Flag.DELETED), true);
            emailFolder.close(true);
            store.close();
        }
    }

    /**
     * Method which loads the emails from the selected folder.
     *
     */
    public void changeFolder(Event event) {
        if (currentFolder.equals(folder.getSelectionModel().getSelectedItem().toString())){
            return;
        }
        currentFolder = folder.getSelectionModel().getSelectedItem().toString();
        pageBox.getChildren().clear();
        pageBox.getChildren().add(new JFXProgressBar());
        try {
            checkForEmails();
        } catch (Exception ex) {
            Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
        }

    }

    /**
     * Method that allows to popup the "no internet connection" alert.
     */
    private void connectionFailed(){
            if (!HandleNet.hasNetConnection()) {
                try {
                    JFXButton button = new JFXButton("Refresh");
                    button.setButtonType(JFXButton.ButtonType.RAISED);
                    button.setStyle("-fx-background-color: #32a4ba");
                    button.minWidth(200);
                    button.setOnAction(actionEvent -> {
                        try {
                            button.setDisable(true);
                            initialize();
                        } catch (Exception e) {
                            if (!HandleNet.hasNetConnection()) {
                                try {
                                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), anchorPane, getClass().getResource("/FXML/noInternet.fxml"));
                                } catch (Exception exc) {
                                    Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                                }
                            } else {
                                try {
                                    checkForEmails();
                                } catch (Exception e1) {
                                    Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                                }
                            }
                        }
                    });
                    pageBox.getChildren().clear();
                    pageBox.getChildren().addAll(button);
                    Convenience.popupDialog(MainPane.getInstance().getStackPane(), MainPane.getInstance().getBorderPane(), getClass().getResource("/FXML/noInternet.fxml"));
                } catch (IOException e1) {
                    Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
                }
            } else{
                Convenience.showAlert(CustomAlertType.ERROR, "Something went wrong. Please, try again later.");
            }
        }
}
