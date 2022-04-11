import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

class email{
    public static void main(String [] args){

        String to="example@gmail.com";//change
        final String user="user";//change
        final String password="password";//change

        // get the session object
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "mail.javatpoint.com");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //compose message
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Message Aleart");

            //create MimeBodyPart object and set your message text
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Attatched is the finished schedule");

            //create new MimeBodyPart object and set DataHandler object to this object
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            String filename = "classFile.txt";//change
            DataSource source = new FileDataSource(filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setFileName(filename);


            //create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            //set the multiplart object to the message object
            message.setContent(multipart );

            //send message
            Transport.send(message);

            System.out.println("message sent....");
        }catch (MessagingException ex) {ex.printStackTrace();}
    }
}