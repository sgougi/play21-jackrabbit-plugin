package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.formdata.LogData;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;

import play.Logger;

@Node
public class Log extends AbstractNode {

	@Field
	private String title;

	@Field
	private String logBody;	
	
	@Collection
	private List<Comment> comments = new ArrayList<Comment>();
	
	@Field(path = true)
	protected String path;
		
	public Log() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getLogBody() {
		return logBody;
	}

	public void setLogBody(final String logBody) {
		this.logBody = logBody;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(final List<Comment> comments) {
		this.comments = comments;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}	
	
	public static Iterable<Log> getLogs(final ObjectContentManager ocm) {
		final QueryManager queryManager = ocm.getQueryManager();
		final Filter filter = queryManager.createFilter(Log.class);
		final Query query = queryManager.createQuery(filter);
		query.addOrderByDescending("createdDate");
		final String jcrExpression = queryManager.buildJCRExpression(query);
		Logger.debug(jcrExpression);
		@SuppressWarnings("unchecked")
		final java.util.Collection<Log> results = ocm.getObjects(query);
		return results;
	}

	public static Log create(final ObjectContentManager ocm, final LogData logData) {
		final Log log = new Log();
		log.setPath("/log");
		log.setLogBody(logData.logBody);
		log.setTitle(logData.title);
		log.setName(logData.name);
		final Date date = new Date();
		log.setCreatedDate(date);
		log.setUpdatedDate(date);
		ocm.insert(log);
		return log;
	}
	
}
