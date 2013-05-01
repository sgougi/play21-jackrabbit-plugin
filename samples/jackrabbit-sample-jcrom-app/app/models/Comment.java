package models;

import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrParentNode;
import org.jcrom.annotations.JcrProperty;

@JcrNode
public class Comment extends AbstractNode
{

	@JcrProperty private String content;
	@JcrParentNode private Log log;
	
    public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

}
