package models;

import java.util.Date;
import java.util.UUID;

import org.jcrom.annotations.JcrIdentifier;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;

@JcrNode
public abstract class AbstractNode {

	@JcrIdentifier protected String id;

	@JcrProperty private Date createdDate;
	
	@JcrProperty private Date updatedDate;	

	@JcrProperty private String name;
	
	@JcrName protected String contentName;
	
	@JcrPath protected String path;
	
	protected AbstractNode() {
		contentName = UUID.randomUUID().toString(); // oops!
	}
	
	public void setPath(final String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}		
	
	public String getId() {
		return id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(final Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
}
