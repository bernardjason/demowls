package bernardjason.weblogic.demo.demojar

import javax.annotation.Resource
import javax.ejb.ActivationConfigProperty
import javax.ejb.MessageDriven
import javax.ejb.MessageDrivenContext
import javax.jms.JMSException
import javax.jms.MessageListener
import javax.jms.ObjectMessage
import bernardjason.weblogic.demo.demoshared.Logging
import javax.mail.internet.MimeMessage
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.Transport
import javax.mail.MessagingException
import java.util.Properties
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@MessageDriven(mappedName = "TheQ", activationConfig = Array(
  new ActivationConfigProperty(propertyName = "acknowledgeMode",
    propertyValue = "Auto-acknowledge"),
  new ActivationConfigProperty(propertyName = "destinationType",
    propertyValue = "javax.jms.Queue")))
class QueueReceive extends MessageListener with Logging {



  def onMessage(message: javax.jms.Message) {

    val msg = message.asInstanceOf[ObjectMessage];
    val emailMessage = msg.getObject().asInstanceOf[EmailMessage];

    try {
      log.info(msg.getJMSCorrelationID() + "  GOT A MESSAGE " + emailMessage);

     	val service = GmailSend.getGmailService();
     	
     	val to=emailMessage.email.replace("@", "+scalawls@");
     	val from=emailMessage.email;

   		val message = GmailSend.createMessageWithEmail(GmailSend.createEmail(to, from, emailMessage.subject,emailMessage.message));

  		service.users().messages().send(from, message).execute();

      log.info("message sent successfully.... from="+from+" to="+to);

    } catch {
      case e: MessagingException => log.error(s"mail error ${emailMessage}", e)
      case e: Exception          => log.error("Problem with MDB", e)
    }
  }
}
