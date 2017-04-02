package bernardjason.weblogic.demo.demoshared;

import javax.ejb.Local;

@Local
public interface HelloLocal {	
    public String sayHello(String email,String subject,String message);
}
