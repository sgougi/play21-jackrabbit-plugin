package controllers;

import static play.data.Form.form;

import java.util.ArrayList;
import java.util.List;

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
	public Application(JackRabbitContext ctx) {
		ocm = ctx.getObjectContentManager();
	}
	
    public Result index() {
		if ( ocm == null ) throw new IllegalStateException("bug");
		return ok(index.render());
    }
  
	public Result postLog() {
		final Form<LogData> logForm = form(LogData.class).bindFromRequest();
		if ( logForm.hasErrors() ) {
			return badRequest(logForm.errorsAsJson());
		}

		// TODO
//		@SuppressWarnings("unused")
//		Log log = LogService.getInstance().create(logForm.get());

		return ok();
	}

	public Result getLogs() {
		// TODO
		List<Log> logModels = new ArrayList<Log>(); 
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
		System.out.println("disupdateddate = " + disupdateFlag);
		
		// TODO
		if ( true )
			return notFound();

		return ok();
	}    
}
