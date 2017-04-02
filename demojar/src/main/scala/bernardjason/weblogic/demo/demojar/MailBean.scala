package bernardjason.weblogic.demo.demojar

import java.util.concurrent.atomic.AtomicInteger

import scala.xml.NodeSeq

import org.apache.log4j.Logger

import fml.flds
import javax.ejb.Stateless
import javax.interceptor.ExcludeDefaultInterceptors
import weblogic.wtc.gwt.XmlFmlCnv
import weblogic.wtc.jatmi.Reply
import weblogic.wtc.jatmi.TPServiceInformation
import weblogic.wtc.jatmi.TypedFML32

@javax.ejb.Remote(Array(classOf[weblogic.wtc.jatmi.TuxedoService]))
@javax.ejb.RemoteHome(classOf[weblogic.wtc.jatmi.TuxedoServiceHome])
@Stateless(mappedName = "MailBean")
@weblogic.javaee.CallByReference
@ExcludeDefaultInterceptors
class MailBean {

  def signature = MailBean.host + "-" + MailBean.ai.incrementAndGet()

  val VERBOSE = true;

  lazy val log = Logger.getLogger(getClass().getName());

  def service(mydata: TPServiceInformation): Reply = {
    {

      log.info("mail bean called");
      val fmlIn = mydata.getServiceData.asInstanceOf[TypedFML32];
      fmlIn.setFieldTables(Array(new flds()))
      var email: Option[String] = None
      var subject: Option[String] = None
      var message: Option[String] = None

      val c = new XmlFmlCnv();
      val input = c.FML32toXML(fmlIn);

      def ifValid(n: NodeSeq): Option[String] = {
        if (!n.isEmpty && n.text.length > 0) return Some(n.text)
        None
      }
      val xml = scala.xml.XML.loadString(input)

      email = ifValid(xml \\ "EMAIL")
      subject = ifValid(xml \\ "SUBJECT")
      message = ifValid(xml \\ "MESSAGE")

      log.info("FML input " + input);

      var xmlDoc: Option[String] = None

      for { e <- email; s <- subject; m <- message } {
        val d = new java.util.Date();
        val sign = signature
        val send = EmailMessage(e, s, s"${m}\n\nFrom EJB ${sign}")

        log.info("Parsed to " + send)
        QueueHelper.sendQueueMessage(sign, send);
        xmlDoc = Some(s"<XML><MESSAGE>time is ${d} look out for ${sign}</MESSAGE></XML>")
      }

      val fields = new TypedFML32(new flds());

      val cc = new XmlFmlCnv();
      val fmlBuffer = cc.XMLtoFML32(
        xmlDoc.getOrElse(s"<XML><MESSAGE>nothing sent, check input</MESSAGE></XML>"),
        fields.getFieldTables());

      val result = cc.FML32toXML(fmlBuffer);

      log.info("FML response " + result);

      mydata.setReplyBuffer(fmlBuffer);
    }

    return (mydata);
  }

  def ejbCreate() {}

  def ejbPostCreate() {}

}
object MailBean {
  val ai = new AtomicInteger(1);
  val host = System.getProperty("weblogic.Name");
}