import com.google.inject.Guice;
import com.google.inject.Injector;

import play.Application;
import play.GlobalSettings;
import com.google.inject.AbstractModule;

public class Global extends GlobalSettings {

	private static final Injector INJECTOR = createInjector();

	@Override
	public <A> A getControllerInstance(Class<A> controllerClass) throws Exception {
		return INJECTOR.getInstance(controllerClass);
	}

	public void onStart(Application app) {
	}

    private static Injector createInjector() {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
            	this.bind(helpers.JackRabbitContext.class).to(helpers.JackRabbitContextImpl.class);
            }
        });
    }
    
}
