package za.co.las.stock.services;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class MailService {
	
	public int sendNotificationEmail(String toAddress, String bodyText) throws Exception {
		return sendEmail(null, toAddress, bodyText);
	}

	public int sendEmailWithQuote(byte[] attachment, String toAddress, String bodyText) throws Exception {
		return sendEmail(attachment, toAddress, bodyText);
	}
	
	private int sendEmail(byte[] attachment, String toAddress, String bodyText) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("liftandshiftquotes@gmail.com","Liftandshift001");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("quotes@liftandshift.co.za"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			message.setSubject("Quotation");
			//message.setText(bodyText);
			
			MimeBodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setContent(bodyText, "text/html; charset=utf-8");            
			
			Multipart multipart = new MimeMultipart();
			if (attachment.length > 0) {
				MimeBodyPart mbp = new MimeBodyPart();
	            mbp.setFileName("AttachedQuote.pdf");
	            
	            DataSource ds = new ByteArrayDataSource(attachment, "application/pdf");
	            mbp.setDataHandler(new DataHandler(ds));
	            multipart.addBodyPart(mbp);
			}
						
			multipart.addBodyPart(msgBodyPart);
		    message.setContent(multipart);
			
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw e;
		}		
		return 1;
	}
}
