package utilities;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Stateless
@ManagedBean (name = "controlMail")
public class MailUtilities
{
	@Resource (name = "mail/MailSession")
	private Session session;

	public void sendMail()
	{
		Message msg = new MimeMessage(session);

		try
		{
			msg.setSubject("My Subject");
			msg.setRecipient(RecipientType.TO, new InternetAddress("tiggytamal@free.fr", "Tiggy"));
			msg.setFrom(new InternetAddress("afpaquetigny@gmail.com", "AfpaQuetigny"));

			BodyPart mbp = new MimeBodyPart();
			mbp.setText("Texte du mail");
			
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
			
			msg.setContent(mp);
			
			
			Transport.send(msg);
		} catch (MessagingException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
