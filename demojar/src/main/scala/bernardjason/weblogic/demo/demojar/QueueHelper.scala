package bernardjason.weblogic.demo.demojar

import java.util.concurrent.atomic.AtomicInteger
import javax.ejb.Local
import javax.ejb.Remote
import javax.ejb.Stateless
import javax.jms.JMSException
import javax.jms.Queue
import javax.jms.QueueConnectionFactory
import javax.jms.QueueSender
import javax.jms.QueueConnection
import javax.jms.QueueSession
import javax.jms.Session
import javax.naming.InitialContext
import javax.naming.NamingException
import bernardjason.weblogic.demo.demoshared.HelloLocal
import bernardjason.weblogic.demo.demoshared.HelloRemote
import bernardjason.weblogic.demo.demoshared.Logging

object QueueHelper extends Logging {
  val QUEUE = "TheQ";
  val JMS_FACTORY = "JMSConnectionFactory";
  
  def sendQueueMessage(correlate: String, queueMessage: EmailMessage) {
    val ctx = new InitialContext();
    var qcon: Option[QueueConnection] = None
    var qsession: Option[QueueSession] = None
    var qsender: Option[QueueSender] = None
    try {
      val qconFactory = ctx.lookup(JMS_FACTORY).asInstanceOf[QueueConnectionFactory];
      qcon = Some(qconFactory.createQueueConnection());
      qsession = Some(qcon.get.createQueueSession(false, Session.AUTO_ACKNOWLEDGE));
      val queue = ctx.lookup(QUEUE).asInstanceOf[Queue];
      qsender = Some(qsession.get.createSender(queue));

      val msg = qsession.get.createObjectMessage();
      msg.setJMSCorrelationID(correlate);
      msg.setObject(queueMessage);

      qcon.get.start();
      qsender.get.send(msg);
    } catch {
      case e: NamingException => log.error("Hello Bean ", e)
      case e: JMSException => log.error("Hello Bean ", e)
    } finally {
      qsender.map { _.close() }
      qsession.map { _.close() }
      qcon.map { _.close }
    }
  }
}