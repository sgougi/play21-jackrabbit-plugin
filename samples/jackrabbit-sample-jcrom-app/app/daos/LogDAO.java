package daos;

import java.util.Date;
import java.util.List;

import javax.jcr.Session;

import models.Log;
import models.formdata.LogData;

import org.jcrom.Jcrom;
import org.jcrom.dao.AbstractJcrDAO;

public class LogDAO extends AbstractJcrDAO<Log>  {

	public LogDAO( Session session, Jcrom jcrom) {
		super(session, jcrom);
	}
	
	public List<Log> list() {
		try {
			return findByXPath("/jcr:root/*[@title!=''] order by @createdDate descending", "*", -1);
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	public Log create(final LogData logData) {
		final Date date = new Date();		
		final Log log = new Log();
		
		log.setPath("/");
		log.setLogBody(logData.logBody);
		log.setTitle(logData.title);
		log.setName(logData.name);
		log.setCreatedDate(date);
		log.setUpdatedDate(date);
		
		return create(log);
	}	
}
