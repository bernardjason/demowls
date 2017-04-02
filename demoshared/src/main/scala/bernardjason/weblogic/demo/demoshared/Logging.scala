package bernardjason.weblogic.demo.demoshared

import org.apache.log4j.Logger

trait Logging {
  var logger:Option[Logger] = None
  
  private def oldlog() = {
    logger.getOrElse( Logger.getLogger( getClass.getName ))
  }
  
  lazy val log = Logger.getLogger( getClass.getName )
  
}