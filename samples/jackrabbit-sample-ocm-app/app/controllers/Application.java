package controllers;

import static play.data.Form.form;

import models.Comment;
import models.Log;
import models.formdata.CommentData;
import models.formdata.LogData;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import com.google.inject.Inject;

import helpers.JackRabbitContext;
import play.*;
import play.data.Form;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
	private ObjectContentManager ocm;
	
	@Inject
	public Application(final JackRabbitContext ctx) {
		ocm = ctx.getObjectContentManager();
	}
	
    public Result index() {
		return ok(index.render());
    }
  
	public Result postLog() {
		final Form<LogData> logForm = form(LogData.class).bindFromRequest();
		if ( logForm.hasErrors() ) {
			return badRequest(logForm.errorsAsJson());
		}
		@SuppressWarnings("unused")
		Log log = Log.create(ocm, logForm.get());
		ocm.save();		
		return ok();
	}

	public Result getLogs() {
		final Iterable<Log> logModels = Log.getLogs(ocm); 
		for ( final Log log : logModels ) {
			Logger.debug(String.format("id = %s , path = %s", log.getId(), log.getPath()));
		}
		return ok(logs.render(logModels));
	}
	
	public Result postComment(String logId) {
		final Form<CommentData> commentForm = form(CommentData.class).bindFromRequest();
		if ( commentForm.hasErrors() ) {
			return badRequest(commentForm.errorsAsJson());
		}
		if ( logId == null ) {
			return notFound();
		}
		boolean disupdateFlag = Boolean.parseBoolean(form().bindFromRequest().get("disupdateFlagLog"));
		Logger.debug(String.format("logId = %s, disupdateddate = %s", logId, disupdateFlag));
		final Log log = (Log)ocm.getObjectByUuid(logId);
		if ( log == null )
			return notFound();		
		@SuppressWarnings("unused")
		final Comment comment = Comment.create(ocm, log, commentForm.get(), disupdateFlag);
		ocm.save();		
		return ok();
	}    
}
