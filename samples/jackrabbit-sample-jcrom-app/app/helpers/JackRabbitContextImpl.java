package helpers;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jcrom.Jcrom;

import com.wingnest.play2.jackrabbit.JCROM;
import com.wingnest.play2.jackrabbit.Jcr;

public class JackRabbitContextImpl implements JackRabbitContext {

	@Override
	public Jcrom getJcrom() {
		return JCROM.createJcrom();
	}

	@Override
	public Session getSession() {
		final String userId = Jcr.getConfig().getUserId();
		final String password = Jcr.getConfig().getPassword();
		final Session session = Jcr.getCurrentSession(userId);
		try {
			return session != null ? session : Jcr.login(userId, password);
		} catch ( RepositoryException e ) {
			throw new RuntimeException(e);
		} 
	}

}
