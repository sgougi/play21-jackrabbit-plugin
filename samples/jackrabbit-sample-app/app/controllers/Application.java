package controllers;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;

import com.google.inject.Inject;

import helpers.JackRabbitContext;
import play.*;
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
        return ok(index.render("Your new application is ready."));
    }
  
}
