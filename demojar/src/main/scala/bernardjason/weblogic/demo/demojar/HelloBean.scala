package bernardjason.weblogic.demo.demojar

import java.util.concurrent.atomic.AtomicInteger

import bernardjason.weblogic.demo.demoshared.HelloLocal
import bernardjason.weblogic.demo.demoshared.HelloRemote
import bernardjason.weblogic.demo.demoshared.Logging
import javax.ejb.Local
import javax.ejb.Remote
import javax.ejb.Stateless

@Stateless(mappedName = "HelloBean")
@Local(Array(classOf[HelloLocal]))
@Remote(Array(classOf[HelloRemote]))
class HelloBean extends HelloLocal with Logging with HelloRemote {
  
  def signature = HelloBean.host + "-" + HelloBean.ai.incrementAndGet()
  
  def sayHello(email:String,subject:String,message:String): String = {

    val sign = signature
    val send = EmailMessage(
        email,subject,
        s"${message}\n\nFrom EJB ${sign}"
        )
    QueueHelper.sendQueueMessage(sign, send);

    log.info(send);
    return "Look out for email with signature "+sign
  }

  
}

object HelloBean {
  val ai = new AtomicInteger(1);
  val host = System.getProperty("weblogic.Name");
}
