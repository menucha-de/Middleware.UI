package havis.net.ui.middleware.client.shared.trigger.model;

public enum PinState {
	ON(".1"), OFF(".0"), IRRELEVANT("");
	
	private String uriPart;
	
	private PinState(String uriPart) {
		this.uriPart = uriPart;
	}
	
	public String getUriPart() {
		return uriPart;
	}
}
