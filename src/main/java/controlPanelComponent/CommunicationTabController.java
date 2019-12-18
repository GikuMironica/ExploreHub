package controlPanelComponent;

import authentification.CurrentAccountSingleton;
import handlers.MessageHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import handlers.Convenience;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import models.Account;


public class CommunicationTabController {
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
    Account account = CurrentAccountSingleton.getInstance().getAccount();


    public void initialize() {

        checkForEmails();
    }


    /**
     *Method which loads the emails from gmail.
     */
    public void checkForEmails()
    {
        try {
            String username = "explorehub.help@gmail.com";
            String password = "cts5-2019";
            String host = "IMAP.gmail.com";
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
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        VBox pageBox = new VBox();
        TextArea messageContent = new TextArea();
        messageContent.setWrapText(true);
        messageContent.setMaxWidth(900);
        messageContent.setMinHeight(300);
        messageContent.setMaxHeight(300);
        messageContent.setEditable(false);
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
            messageContent.setFont(Font.font("Serif", FontWeight.BOLD, 16));
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
           e.printStackTrace();
        }
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
    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws IOException, MessagingException {

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
        TextArea textArea = new TextArea();
        textArea.setMinHeight(250);
        textArea.setMinWidth(500);
        String text = "Dear " + name.getText() + " " + surname.getText() +",\n"
                    + "Thank you for your email!\n"
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
        button.setOnAction(actionEvent -> {
            try {
                String subject = messages[mailSelected].getSubject();
                MessageHandler messageHandler = MessageHandler.getMessageHandler();
                messageHandler.sendEmail(textArea.getText(), subject, email.getText());
                dialog.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        content.setActions(button);
        dialog.show();
    }

    public void openHomepage(MouseEvent mouseEvent) {
        try {
            Convenience.switchScene(mouseEvent,getClass().getResource("/FXML/mainUI.fxml") );
        } catch (IOException e) {
            Convenience.showAlert(Alert.AlertType.ERROR,
                    "Error", "Something went wrong", "Please, try again later");
        }
    }
}
