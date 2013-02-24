package helpers;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import com.wingnest.play2.jackrabbit.OCM;


public class JackRabbitContextImpl implements JackRabbitContext {

	public ObjectContentManager getObjectContentManager() {
		return OCM.getOCM();
	}
	
}
