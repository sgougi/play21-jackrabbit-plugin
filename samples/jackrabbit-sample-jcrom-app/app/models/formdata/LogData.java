package models.formdata;

import play.data.validation.Constraints.Required;

public class LogData {

	@Required
	public String title;
	@Required
	public String name;
	@Required
	public String logBody;

	//
	
	public String getTitle() {
		return title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getLogBody() {
		return logBody;
	}
	public void setLogBody(final String logBody) {
		this.logBody = logBody;
	}

}
