package daos;

import java.util.Date;

import javax.jcr.Session;

import models.Comment;
import models.Log;
import models.formdata.CommentData;

import org.jcrom.Jcrom;
import org.jcrom.dao.AbstractJcrDAO;

import play.Logger;

public class CommentDAO extends AbstractJcrDAO<Comment> {

	public CommentDAO(Session session, Jcrom jcrom) {
		super(session, jcrom);		
	}
	
	public Comment create(final Log log, final CommentData commentData, final boolean disupdateFlag) {
		final Comment comment = new Comment();
		Logger.debug(String.format("log path = %s", log.getPath()));
		comment.setPath(log.getPath());
		comment.setName(commentData.name);
		comment.setContent(commentData.content);
		final Date date = new Date();
		comment.setCreatedDate(date);
		comment.setUpdatedDate(date);
		log.getComments().add(comment);
		if(!disupdateFlag)
			log.setUpdatedDate(date);

		LogDAO logDAO = new LogDAO(session, jcrom);
		logDAO.update(log);

		return create(comment);
	}	

}
