package models;

import java.util.ArrayList;
import java.util.List;

import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrProperty;

@JcrNode
public class Log extends AbstractNode {
	
	@JcrProperty
	private String title;

	@JcrProperty
	private String logBody;	
	
	@JcrChildNode(createContainerNode=false) private List<Comment> comments = new ArrayList<Comment>();
	
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

}
