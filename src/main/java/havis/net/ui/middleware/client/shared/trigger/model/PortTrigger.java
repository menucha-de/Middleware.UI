package havis.net.ui.middleware.client.shared.trigger.model;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import havis.net.ui.middleware.client.utils.Utils;

public class PortTrigger extends Trigger {

	public enum PinType {
		INPUT("in"), OUTPUT("out");

		private String param;

		private PinType(String param) {
			this.param = param;
		}

		public static PinType getValue(String value) {
			for (PinType t : PinType.values()) {
				if (t.param.equals(value)) {
					return t;
				}
			}
			return null;
		}
		
		public String getParam(){
			return param;
		}
	}
	
	public final static int PIN_ANY = -1;
	public final static int PIN_CUSTOM = -2;

	private final static int GROUP_READER = 1;
	private final static int GROUP_PINTYPE = 2;
	private final static int GROUP_ID = 6;
	private final static int GROUP_STATE = 8;
	private final static String pattern = "^urn:havis:ale:trigger:port:(.+):((in)|(out))(:(\\d+)(\\.([01]))?)?$";
	private final static String base = "urn:havis:ale:trigger:port:";

	private String reader;
	private PinType pinType;
	private PinState state;
	private int id = PIN_ANY;

	public PortTrigger() {
		this.scheme = TriggerScheme.HAVIS;
		this.type = TriggerType.PORT;
		this.state = PinState.IRRELEVANT;
	}

	public PortTrigger(String uri) {
		this();
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(uri);
		if (result != null) {
			this.reader = result.getGroup(GROUP_READER);
			this.pinType = PinType.getValue(result.getGroup(GROUP_PINTYPE));
			try {
				this.id = Integer.parseInt(result.getGroup(GROUP_ID));
			} catch (NumberFormatException e) {
				this.id = PIN_ANY;
			}
			try {
				String stateGroup = result.getGroup(GROUP_STATE);
				if (stateGroup == null || Utils.isNullOrEmpty(stateGroup)) {
					this.state = PinState.IRRELEVANT;
				} else {
					int state = Integer.parseInt(stateGroup);
					if (state == 0)
						this.state = PinState.OFF;
					else
						this.state = PinState.ON;
				}
			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public String toUri() {
		return base + (reader != null ? reader : "") + ":"
				+ (pinType != null ? pinType.getParam() : PinType.INPUT.getParam())
				+ (PIN_ANY == id ? "" : ":" + id + state.getUriPart());
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public PinType getPinType() {
		return pinType;
	}

	public void setPinType(PinType pinType) {
		this.pinType = pinType;
	}

	public PinState getState() {
		return state;
	}

	public void setState(PinState state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
