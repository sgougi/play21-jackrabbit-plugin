package models;

import java.util.ArrayList;
import java.util.List;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(extend=AbstractNode.class)
public class Log extends AbstractNode {

	@Field
	private String title;

	@Field
	private String logBody;	
	
	@Collection
	private List<Comment> comments;
	
	@Field(path = true)
	protected String path;
	
	
	public Log(String path) {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogBody() {
		return logBody;
	}

	public void setLogBody(String logBody) {
		this.logBody = logBody;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}	
	
	public List<Comment> getCommentsOrderByCreatedDateAsc() {
		return new ArrayList<Comment>();
	}
}
