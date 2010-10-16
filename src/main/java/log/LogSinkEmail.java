package log;

import java.io.StringBufferInputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPTransport;

public class LogSinkEmail extends LogSink {
	
	private Address[] recipients;
	private Address sender;
	private String subject;
	private String mailserver;

	public LogSinkEmail(String name, LogLevel loglevel, Properties props) throws AddressException {		
		super(name, loglevel, props);
		
		recipients = parseRecipients(props.getProperty("email.recipients", "jan.tisje@004gmbh.de"));
		sender = new InternetAddress(props.getProperty("email.sender", "jan.tisje@004gmbh.de"));
		subject = props.getProperty("email.subject", "Error: {1}");
		mailserver = props.getProperty("email.server", "mail");
	} 

	private Address[] parseRecipients(String property) {
		String[] recipients = property.split(";|,");
		Address[] result = new Address[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			try {
				result[i] = new InternetAddress(recipients[i]);
			} catch (AddressException e) {
				result[i] = null;
				e.printStackTrace();
			}
		}
		return null;
	}

	public void log(LogMessage message) {
		if (shouldLog(message)) {			
			StringBuffer mailbody = new StringBuffer();
			if (queue != null) {
				synchronized (queue.getLock()) {
					while (!queue.isEmpty()) {
						mailbody.append(queue.dequeue().toString());
						mailbody.append('\n');
					}
				}
			}
			mailbody.append(message.toString());
			mailbody.append('\n');
			sendEmail(new Properties(), mailbody.toString());
		} else {
			if (queue != null) {
				queue.add(message);
			}
		}		
	}
	
	public void log(String message) {
		// mh? do nothing?
	}
	
	private void sendEmail(Properties props, String text) {
		
		System.out.println(" --- EMAIL --- ");
		System.out.print(text);
		System.out.println(" --- EMAIL --- ");
		Session session = Session.getInstance(props);
		MimeMessage message = new MimeMessage(session);
		Multipart part = new MimeMultipart();
		try {
			part.addBodyPart(new MimeBodyPart(new StringBufferInputStream(text)));
			
			message.setContent(part);
			message.setSender(sender);
			message.setRecipients(RecipientType.TO, recipients);			
			message.setSubject(subject);
			
//			SMTPTransport.send(message);
			
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}
