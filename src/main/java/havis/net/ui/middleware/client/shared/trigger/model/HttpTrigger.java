package havis.net.ui.middleware.client.shared.trigger.model;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class HttpTrigger extends Trigger {
	
	private final static int GROUP_NAME = 1;
	private final static String pattern = "^urn:havis:ale:trigger:http:(.+)$";
	private final static String base = "urn:havis:ale:trigger:http:";
	
	private String name;

	public HttpTrigger(){
		this.scheme = TriggerScheme.HAVIS;
		this.type = TriggerType.HTTP;
	}
	
	public HttpTrigger(String uri) {
		this.scheme = TriggerScheme.HAVIS;
		this.type = TriggerType.HTTP;
		
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(uri);
		if (result != null) {
			this.name = result.getGroup(GROUP_NAME);			
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toUri() {
		return base + (name != null ? name : "");
	}
}
