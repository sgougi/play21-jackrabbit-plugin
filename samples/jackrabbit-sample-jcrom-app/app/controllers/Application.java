package controllers;

import static play.data.Form.form;

import java.util.List;

import javax.jcr.Session;

import models.Comment;
import models.Log;
import models.formdata.CommentData;
import models.formdata.LogData;

import org.jcrom.Jcrom;

import com.google.inject.Inject;

import daos.CommentDAO;
import daos.LogDAO;

import helpers.JackRabbitContext;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

	private Jcrom jcrom;
	private Session session;
	  
	@Inject
	public Application(final JackRabbitContext ctx) {
		jcrom = ctx.getJcrom();
		session = ctx.getSession();
	}
	
    public Result index() {
		return ok(index.render());
    }
  
	public Result postLog() {
		try {
			final Form<LogData> logForm = form(LogData.class).bindFromRequest();
			if ( logForm.hasErrors() ) {
				return badRequest(logForm.errorsAsJson());
			}
			final LogDAO logDao = new LogDAO(session, jcrom);
			@SuppressWarnings("unused")
			final Log log = logDao.create(logForm.get());
			session.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok();
	}

	public Result getLogs() {
		final LogDAO logDao = new LogDAO(session, jcrom);
		final List<Log> logModels = logDao.list();
		for ( final Log log : logModels ) {
			Logger.debug(String.format("id = %s , path = %s", log.getId(), log.getPath()));
		}
		return ok(logs.render(logModels));
	}
	
	public Result postComment(String logId) {
		try {
			final Form<CommentData> commentForm = form(CommentData.class).bindFromRequest();
			if ( commentForm.hasErrors() ) {
				return badRequest(commentForm.errorsAsJson());
			}
			if ( logId == null ) {
				return notFound();
			}
			boolean disupdateFlag = Boolean.parseBoolean(form().bindFromRequest().get("disupdateFlagLog"));
			Logger.debug(String.format("logId = %s, disupdateddate = %s", logId, disupdateFlag));
			final LogDAO logDao = new LogDAO(session, jcrom);	
			final Log log = logDao.loadById(logId);
			if ( log == null )
				return notFound();

			final CommentDAO commentDao = new CommentDAO(session, jcrom);
			@SuppressWarnings("unused")
			final Comment comment = commentDao.create(log, commentForm.get(), disupdateFlag);			
			session.save();
		} catch (Exception e){
			e.printStackTrace();
		}
		return ok();
	}    
}
