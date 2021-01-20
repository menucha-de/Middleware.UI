package havis.net.ui.middleware.client.shared.trigger.model;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public abstract class Trigger {
	private final static int GROUP_SCHEME = 1;
	private final static int GROUP_TYPE = 2;
	private final static String pattern = "urn:([a-z]+):ale:trigger:([a-z]+):(.*)";

	protected TriggerScheme scheme;
	protected TriggerType type;

	protected Trigger() {

	}

	/**
	 * Determines the trigger scheme.
	 * 
	 * @param uri
	 * @return trigger scheme or null
	 */
	public static TriggerScheme getTriggerScheme(String uri) {
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(uri);
		if (result != null) {
			String scheme = result.getGroup(GROUP_SCHEME);
			return TriggerScheme.getValue(scheme);
		}
		return null;
	}
	
	/**
	 * Determines the trigger type.
	 * 
	 * @param uri
	 * @return trigger type or null
	 */
	public static TriggerType getTriggerType(String uri) {
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(uri);
		if (result != null) {
			String type = result.getGroup(GROUP_TYPE);
			return TriggerType.getValue(type);
		}
		return null;
	}
	
	public TriggerScheme getScheme() {
		return scheme;
	}

	public void setScheme(TriggerScheme scheme) {
		this.scheme = scheme;
	}

	public TriggerType getType() {
		return type;
	}

	public void setType(TriggerType type) {
		this.type = type;
	}

	public abstract String toUri();
}
