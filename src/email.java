
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

class email {
    public static void main(String[] args) {
        final String username = "username";
        final String password = "password";
        String fromEmail = "fromemail@email.com";
        String toEmail = "toEmail@email.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthenication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            //subject line
            message.setSubject("Subject Line"); // change this

            Multipart emailContent = new MimeMultipart();

            // text part of email
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Multipart text"); // change this

            // attatchment part of email
            MimeBodyPart txtAttatchment = new MimeBodyPart();
            txtAttatchment.attachFile("FinishedSchedule.txt");

            // attatch all parts
            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(txtAttatchment);

            message.setContent(emailContent);

            Transport.send(message);
            System.out.println("Message sent");
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}