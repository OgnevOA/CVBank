package telran.b7a.employer.notifications.implementations;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import telran.b7a.employer.notifications.interfaces.NotifyUser;

@Component
public class NotifyUserByEmail implements NotifyUser {
	@Value("${MAIL_PASS}")
	String MAIL_PASS;


    @Override
    public void notify(String address, Object... data) {
        //data[0] - token
        //data[1] - name
        //data[2] - expiration date
        {
            // Recipient's email ID needs to be mentioned.
            String to = address;

            // Sender's email ID needs to be mentioned
            String from = "ognevoa94@gmail.com";

            // Assuming you are sending email from through gmails smtp
            String host = "smtp.gmail.com";

            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
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
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("Confirm your CV relevance");

                // Now set the actual message
//            message.setText("This is actual message");
                message.setContent(
                        "<h1>Hello "
                                + data[1] +
                                "!</h1>" +
                                "<p>To confirm your CV click " +
                                "<a href=\"http://localhost:8080/cvbank/notify/" + data[0] + "\">here</a></p>" +
                                "<p>This link is will expire after " +
                                data[2] +
                                "</p>"
                        , "text/html");

                System.out.println("sending...");
                // Send message
                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
            //TODO
            System.out.println("Email was sent to: " + address);
        }
    }
}
