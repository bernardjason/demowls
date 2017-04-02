package bernardjason.weblogic.demo.demowar

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jws.WebService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.apache.log4j.Logger;
import bernardjason.weblogic.demo.demoshared.Logging;
import bernardjason.weblogic.demo.demoshared.HelloRemote
import bernardjason.weblogic.demo.demoshared.HelloLocal
import javax.naming.InitialContext

class HelloWorldImpl extends HelloWorld with Logging { 
  
  // ***********************
  // https://docs.oracle.com/cd/E19798-01/821-1841/girgn/index.html
  // ***********************
  
  val ctx = new javax.naming.InitialContext();
  var hello:HelloLocal = ctx.lookup("java:app/demojar/HelloBean!bernardjason.weblogic.demo.demoshared.HelloLocal").asInstanceOf[HelloLocal]
 
  def sayHi(email:String,subject:String,message:String):String = {
    val sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

		val list2 = ctx.lookup("java:global").asInstanceOf[javax.naming.Context].list("");
		while (list2.hasMore()) {
				     log.info("++++"+list2.next().getName());
		}		   
		
    val r = hello.sayHello(email,subject,message)
    log.info("response from ejb "+r);
    "success "+sdf.format(new Date()) + " look out for something signed "+r
  }

 
}
