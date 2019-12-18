package handlers;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Class that handles email confirmation messages.
 */
public class MessageHandler {

    private static MessageHandler messageHandler;
    private final String username = "iexplore.confirmation@gmail.com";
    private final String password = "cts5-2019";
    private Properties props;
    private Authenticator authenticator;

    /**
     * Default constructor.
     */
    private MessageHandler(){

        // Get a Properties object
        props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }

    /**
     * Getter for a message handler singleton.
     * @return instance of message Handler.
     */
    public static MessageHandler getMessageHandler(){
        if(messageHandler == null){
            messageHandler = new MessageHandler();
        }
        return messageHandler;

    }


    /**
     * Method sends a confirmation email with confirmation code to a user.
     * @param code confirmation code as a String.
     * @param recipientEmail email address of a user as a String.
     * @throws MessagingException
     */
    public void sendRecoveryConfirmation(String code, String recipientEmail) throws MessagingException{
        String title = "noreply_iExplore password recovery ";
        String message = "Please find your confirmation code below:\n" +
                        code + " \n" +
                        "Please confirm password recovery by entering this code to corresponding window\n" +
                "Your iExplore team.";

        MimeMessage msg = prepareMessage(title,message, recipientEmail);
        Transport.send(msg);

    }

    /**
     * Method sends a confirmation email with confirmation code to a user.
     * @param password new password as a String.
     * @param recipientEmail email address of a user as a String.
     * @throws MessagingException
     */
    public void sendNewPassword(String password, String recipientEmail) throws MessagingException{
        String title = "noreply_iExplore password recovery ";
        String message = "Please find your temporary password below:\n" +
                password + " \n" +
                "Please login using this password and change it in zour profile settings.\n" +
                "Your iExplore team.";
        MimeMessage msg = prepareMessage(title,message, recipientEmail);
        Transport.send(msg);
    }

    /**
     * Method sends confirmation email to user + pdf invoice
     * @param letter confirmation message as a String
     * @param recipientEmail email address of a user as a String
     * @param filename Name of pdf file to be sent
     * @throws MessagingException
     */
    public void sendConfirmation(String letter, String recipientEmail, String filename) throws MessagingException{
        String title = "noreply_iExplore Booking Confirmation ";

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        MimeMessage msg = prepareMessage(title, letter, recipientEmail);
        msg.setContent(multipart);

        Transport.send(msg);
    }

    /**
     * Method sends confirmation email to user
     * @param letter confirmation message as a String
     * @param recipientEmail email address of a user as a String
     * @throws MessagingException
     */
    public void sendConfirmation(String letter, String recipientEmail) throws MessagingException{
        String title = "noreply_iExplore Booking Confirmation ";

        MimeMessage msg = prepareMessage(title, letter, recipientEmail);
        Transport.send(msg);
    }


    /**
     * Method that prepares message prior sending.
     * @param title subject of a message as a String.
     * @param text message content as a String.
     * @param recipientEmail user's email address as a String.
     * @return message fully prepared message of a type MimeMessage.
     * @throws MessagingException
     */
    private MimeMessage prepareMessage(String title, String text, String recipientEmail)throws MessagingException{

        Session session = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
        message.setSubject(title);
        message.setText(text, "utf-8");
        return message;
    }

    /**
     * Method that prepares message prior sending.
     * @param subject subject of a message as a String.
     * @param message message content as a String.
     * @param recipientEmail user's email address as a String.
     * @return message fully prepared message of a type MimeMessage.
     * @throws MessagingException
     */
    public void sendEmail(String message, String subject, String recipientEmail) throws MessagingException{
        MimeMessage msg = prepareMessage(subject, message, recipientEmail);
        Transport.send(msg);
    }

}
