package havis.net.ui.middleware.client.shared.trigger.model;

public enum TriggerScheme {
	EPC_GLOBAL("epcglobal", new TriggerType[] { TriggerType.RTC }), HAVIS("havis",
			new TriggerType[] { TriggerType.HTTP, TriggerType.PORT });

	private String scheme;
	private TriggerType[] types;

	private TriggerScheme(String scheme, TriggerType[] types) {
		this.scheme = scheme;
		this.types = types;
	}

	public TriggerType[] getTriggerTypes() {
		return types;
	}

	public static TriggerScheme getValue(String scheme) {
		for (TriggerScheme s : TriggerScheme.values()) {
			if (s.scheme.equals(scheme)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return scheme;
	}
}
