package helpers;

import javax.jcr.Session;

import org.jcrom.Jcrom;

public interface JackRabbitContext {

	Jcrom getJcrom();
	Session getSession();
	
}
