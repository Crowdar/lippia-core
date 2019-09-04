package com.crowdar.email;

import com.crowdar.core.PropertyManager;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Configuration and sending of mail by TLS attaching a file.
 */
public class EmailUtil {

	private static Properties config() {
		
		Properties props = new Properties();
        props.put("mail.smtp.host", PropertyManager.getProperty("mail.smtp"));
        props.put("mail.smtp.port", PropertyManager.getProperty("mail.port"));
        props.put("mail.debug", PropertyManager.getProperty("mail.debug"));
        return props;
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
			Session session = Session.getDefaultInstance(EmailUtil.config());
					
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

}
