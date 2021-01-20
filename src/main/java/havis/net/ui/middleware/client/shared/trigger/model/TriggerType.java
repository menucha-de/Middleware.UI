package havis.net.ui.middleware.client.shared.trigger.model;

public enum TriggerType {
	HTTP("http"),
	PORT("port"),
	RTC("rtc");
	
	private String type;
	
	private TriggerType(String type){
		this.type = type;
	}
	
	public static TriggerType getValue(String type) {
		for (TriggerType t : TriggerType.values()) {
			if (t.type.equals(type)) {
				return t;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
