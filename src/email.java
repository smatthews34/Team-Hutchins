
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

class email {
    public static void main(String[] args) throws Exception{
        // Recipient's email
        String to = "gerellocm19@gcc.edu";

        // Sender's email
        String from = "myschedulinghelper@gmail.com";

        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("myschedulinghelper@gmail.com", "teamHutchins1!");

            }

        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Attatched Class Schedule");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

                File f =new File("classFile.txt");

                attachmentPart.attachFile(f);
                textPart.setText("Here is your completed class schedule");
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            System.out.println("sending...");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully!");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }

    }
}