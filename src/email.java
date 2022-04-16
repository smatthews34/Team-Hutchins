/*
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

// when added to main need to change to email the final schedule. For testing I emailed other txt files in our folder
class email {
    public static void emailSender(String to) throws Exception{
        // Recipient's email
        //String to = "gerellocm19@gcc.edu";

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

        // Get the Session object
        // pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("myschedulinghelper@gmail.com", "teamHutchins1!");

            }

        });

        try {
            MimeMessage message = new MimeMessage(session);

            // Set who the email is from
            message.setFrom(new InternetAddress(from));

            // Set who is recieving the email
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set the subject of the email
            message.setSubject("Attatched Class Schedule");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            // add everything to email
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

    //testing
    public static void main(String[] args) throws Exception {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter your email address");
        String to = scnr.next();
        emailSender(to);
    }
}
*/