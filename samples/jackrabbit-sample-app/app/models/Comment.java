package models;

import java.util.Date;

import models.formdata.CommentData;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;


@Node
public class Comment extends AbstractNode
{

	@Field private String content;

    public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public static Comment create(final ObjectContentManager ocm, final Log log, final CommentData commentData, final boolean disupdateFlag) {
		final Comment comment = new Comment();
		comment.setName(commentData.name);
		comment.setContent(commentData.content);
		final Date date = new Date();
		comment.setCreatedDate(date);
		comment.setUpdatedDate(date);
		log.getComments().add(comment);
		if(!disupdateFlag)
			log.setUpdatedDate(date);
		ocm.update(log);
		return comment;
	}

}
