package com.crowdar.email;

import com.crowdar.core.PropertyManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration and sending of mail by TLS attaching a file.
 */
public class EmailUtil {
    private static final String DEFAULT_EMAIL_FOLDER = "INBOX";
    private static Session session;

    private static Properties getProperties() {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", PropertyManager.getProperty("email.protocol"));
        properties.put("mail.pop3s.ssl.trust", "*");
        properties.put("mail.pop3s.host", PropertyManager.getProperty("email.host"));
        properties.put("mail.pop3s.port", PropertyManager.getProperty("email.port"));

        return properties;
    }

    /**
     * Utility method to send simple HTML email
     */
    public static void sendReportEmail() {

        String from = PropertyManager.getProperty("report.mail.from");
        String to = PropertyManager.getProperty("report.mail.to");
        String cc = PropertyManager.getProperty("report.mail.cc");
        String subject = PropertyManager.getProperty("report.mail.subject");

        try {
            System.out.println("Sending Email...");
            Session session = Session.getDefaultInstance(EmailUtil.getProperties());

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from, from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());

            System.out.println("Recipients: " + message.getAllRecipients());

            String reportLink = System.getProperty("crowdar.email.report.public.url");
            message.setText("Report link: \n" + reportLink, "UTF-8");

            Transport.send(message);
            System.out.println("Email Sent Successfully!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all messages that contain the subject from an specified email target
     *
     * @param folderParam is the folder
     * @param email       is the email target
     * @param password    is the email's password
     * @param subject     is the subject that we are looking
     * @throws NoSuchProviderException,MessagingException errors getting messages from target email
     * @author Dario Vallejos
     */
    public static Message[] getMessages(String folderParam, String email, String password, String subject) throws MessagingException {
        Store store = getConnection(email, password);
        try {
            Folder folder = store.getFolder(folderParam);
            if (!folder.exists()) {
                throw new MessagingException("The folder DO NOT EXIST in email address");
            }
            folder.open(Folder.READ_ONLY);

            SearchTerm bySubject = new SearchTerm() {
                @Override
                public boolean match(Message m) {
                    try {
                        return m.getSubject().equals(subject);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };
            Message[] messages = folder.search(bySubject);

            return messages;
        } finally {
            session.getTransport().close();
        }
    }

    /**
     * Verify that email is present in the email address target folder
     *
     * @param folder   is the folder
     * @param email    is the email target
     * @param password is the email target's password
     * @param subject  is the subject that we are looking
     * @param from     who is sending the email to email target
     * @throws MessagingException
     * @author Dario Vallejos
     */
    public static boolean verifyEmailIsPresent(String email, String password, String subject, String from, String folder) throws MessagingException {
        Message[] messages = getMessages(folder, email, password, subject);
        if (messages == null || messages.length == 0) {
            return false;
        }

        for (Message m : messages) {
            String mfrom = m.getFrom()[0].toString();
            String msubject = m.getSubject();
            if (from.isEmpty()) {
                from = mfrom;
            }
            if (mfrom.contains(from) && msubject.contains(subject)) {
                return true;
            }
        }
        return false;
    }

    public static boolean verifyEmailIsPresent(String email, String password, String subject, String from) throws MessagingException {
        return verifyEmailIsPresent(email, password, subject, from, DEFAULT_EMAIL_FOLDER);
    }

    public static boolean verifyEmailIsPresent(String email, String password, String subject) throws MessagingException {
        return verifyEmailIsPresent(email, password, subject, "", DEFAULT_EMAIL_FOLDER);
    }

    /**
     * Get links from email account target folder that matches subject, from, sentDate(day&hour) @param
     *
     * @param folder   is the folder
     * @param email    is the email target
     * @param password is the email target's password
     * @param subject  is the subject that we are looking
     * @throws MessagingException
     */
    public static List<String> getLinksFromEmail(String folder, String email, String password, String subject) throws MessagingException {
        String urlResetPassword;
        List<String> links = new ArrayList<String>();
        Message[] messages = getMessages(folder, email, password, subject);
        if (messages == null || messages.length == 0) {
            return null;
        }
        List<Message> messageList = new ArrayList<Message>();
        messageList.add(messages[messages.length - 1]);
        for (Message m : messageList) {
            try {
                Object content = m.getContent();
                String mailBody = "";
                if (content instanceof String) {
                    mailBody = (String) content;
                } else if (content instanceof Multipart) {
                    MimeMultipart mimeMultipart = (MimeMultipart) m.getContent();
                    mailBody = getTextFromMimeMultipart(mimeMultipart);
                }
                Matcher matcher = urlPattern.matcher(mailBody);
                while (matcher.find()) {
                    int matchStart = matcher.start(1);
                    int matchEnd = matcher.end();
                    urlResetPassword = mailBody.substring(matchStart, matchEnd);
                    links.add(urlResetPassword);
                }

            } catch (Exception e) {
                throw new MessagingException(e.getMessage());
            }
        }
        return links;
    }

    public static List<String> getLinksFromEmail(String email, String password, String subject) throws MessagingException {
        return getLinksFromEmail(DEFAULT_EMAIL_FOLDER, email, password, subject);
    }

    public static String getSMSFromEmail(String folder, String email, String password, String subject) throws MessagingException {
        Message[] messages = getMessages(folder, email, password, subject);
        String sms;
        if (messages == null || messages.length == 0) {
            return null;
        }

        Message message = messages[messages.length - 1];
        Pattern pattern = Pattern.compile("\\b(?=\\w)\\d+");
        try {
            Object content = message.getContent();
            String mailBody = (String) content;
            Matcher m = pattern.matcher(mailBody);
            m.find();
            sms = m.group(0);
        } catch (Exception e) {
            throw new MessagingException(e.getMessage());
        }
        return sms;
    }

    public static String getSMSFromEmail(String email, String password, String subject) throws MessagingException {
        return getSMSFromEmail(DEFAULT_EMAIL_FOLDER, email, password, subject);
    }

    /**
     * This method is used to get email's content en text format when emails are MimeMultipart type
     *
     * @param mimeMultipart
     * @return String email content
     * @author Dario Vallejos
     */
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    private static Store getConnection(String email, String password) throws MessagingException {
        session = Session.getDefaultInstance(EmailUtil.getProperties(), null);
        Store store;

        store = session.getStore(PropertyManager.getProperty("email.protocol"));
        store.connect(PropertyManager.getProperty("email.host"), Integer.parseInt(PropertyManager.getProperty("email.port")), email, password);
        return store;
    }

    public static String getMailContent(String email, String password, String subject, String folder) throws MessagingException, IOException, ArrayIndexOutOfBoundsException {
        Store store = getConnection(email, password);
        try {
            Folder inbox = store.getFolder(folder);
            inbox.open(Folder.READ_ONLY);
            SearchTerm bySubject = new SearchTerm() {
                @Override
                public boolean match(Message m) {
                    try {
                        return m.getSubject().equals(subject);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };

            Message[] messages = inbox.search(bySubject);

            Object firstMessage = messages[0].getContent();
            if (firstMessage instanceof Multipart) {
                return getTextFromMimeMultipart((MimeMultipart) messages[0].getContent());
            } else {
                return firstMessage.toString();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Email was not found";
        } finally {
            session.getTransport().close();
        }
    }

    public static Document getTextFromHtml(String html) {
        return Jsoup.parse(html);
    }

    public static String getMailContent(String email, String password, String subject) throws IOException, MessagingException {
        return getMailContent(email, password, subject, DEFAULT_EMAIL_FOLDER);
    }

    /**
     * Get all messages
     *
     * @param folderParam is the folder
     * @param email       is the email target
     * @param password    is the email's password
     * @throws NoSuchProviderException,MessagingException errors getting messages from target email
     * @author Guillermo Aguirre
     */
    public static void deleteAllMessages(String folderParam, String email, String password) throws MessagingException {
        Store store = getConnection(email, password);
        try {
            Folder folder = store.getFolder(folderParam);
            if (!folder.exists()) {
                throw new MessagingException("The folder DO NOT EXIST in email address");
            }
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                message.setFlag(Flags.Flag.DELETED, true);
            }

            folder.close(true);
        } finally {
            session.getTransport().close();
        }
    }

    public static void deleteAllMessages(String email, String password) throws MessagingException {
        deleteAllMessages(DEFAULT_EMAIL_FOLDER, email, password);
    }


    /**
     * Pattern for recognizing a URL, based off RFC 3986
     */
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);


}
