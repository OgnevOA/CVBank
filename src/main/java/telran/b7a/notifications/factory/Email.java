package telran.b7a.notifications.factory;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import telran.b7a.notifications.factory.types.EmailType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    EmailType emailInfo;
    @Value("${MAIL_PASS}")
    String MAIL_PASS = System.getenv("MAIL_PASS");
    String to;
    String from = "ognevoa94@gmail.com";
    String host = "smtp.gmail.com";
    Properties properties;
    MimeMessage message;

    public Email(EmailType emailInfo) {
        this.emailInfo = emailInfo;
        System.out.println(MAIL_PASS);
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ognevoa94@gmail.com", MAIL_PASS);
            }
        });
        session.setDebug(true);
        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInfo.getAddress()));
            message.setSubject(emailInfo.getSubject());
            message.setContent(emailInfo.getBody(), emailInfo.getType());
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void send() {
        try {
            Transport.send(this.message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
