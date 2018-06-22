package com.crowdar.email;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Configuracion y envio de mail por TLS adjuntando un archivo.
 */
public class EmailUtil {

    private static String fromEmail;
    private static String password;

    private static Properties config(){
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailProperties.put("mail.smtp.port", "587");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        return mailProperties;
    }

    /**
     * Utility method to send simple HTML email
     * @param toEmail
     * @param subject
     * @param body
     */
    public static void sendEmail(String toEmail, String subject, String body){
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        InputStream gmailAccountFile = null;

        try {
            encryptor.setPassword("abhinav");

            Properties props = new EncryptableProperties(encryptor);

            gmailAccountFile = new FileInputStream("gmailAccount.properties");
            props.load(gmailAccountFile);

            fromEmail = props.getProperty("gmailUser");
            password = props.getProperty("gmailPassword");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (gmailAccountFile != null) {
                try {
                    gmailAccountFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Properties mailProperties = EmailUtil.config();
        try
        {
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            };
            Session session = Session.getInstance(mailProperties, auth);

            MimeMessage msg = new MimeMessage(session);

            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, "Crowdar"));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            BodyPart messageBodyPart = new MimeBodyPart();
            BodyPart attachmentBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            String filename = System.getProperty("user.dir") + "/test-output/CrowdarReport.html";
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setText(body);
            attachmentBodyPart.setFileName("Crowdar report");


            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            msg.setContent(multipart);

            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
