package authentification;


import org.junit.Test;
import javax.mail.MessagingException;
import static org.junit.Assert.*;


public class MessageHandlerTest {
    MessageHandler messageHandler;

    @Test
    public void getMessageHandler() {
        messageHandler = MessageHandler.getMessageHandler();
        assertEquals(true, messageHandler != null);
    }

    @Test
    public void sendRecoveryConfirmation() {
        messageHandler = MessageHandler.getMessageHandler();
        try {
            messageHandler.sendRecoveryConfirmation("31c0797d-07f0-4197-b9a4-4ab251d8ab17","marmiss@mail.hs-ulm.de");
        }catch (MessagingException me){
            fail(me.getMessage());
        }
    }

    @Test
    public void sendNewPassword() {
        messageHandler = MessageHandler.getMessageHandler();
        try {
            messageHandler.sendRecoveryConfirmation("Hk3Kh1lFG5","marmiss@mail.hs-ulm.de");
        }catch (MessagingException me){
            fail(me.getMessage());
        }
    }
}