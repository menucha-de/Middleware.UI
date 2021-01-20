package havis.net.ui.middleware.client.place;

public enum ListType {
	CM(false, null), LR(false, null), TM(false, null), EC(true, null), CC(true, null), PC(true, null), DS(false, null), DSCA(false, "/caches"), DSAS(false, "/associations"), DSRN(false, "/randoms");
	
	private boolean hasSubscribers;
	private String uri;
	
	private ListType(boolean hasSubscribers, String uri) {
		this.hasSubscribers = hasSubscribers;
		this.uri = uri;
	}

	public boolean isHasSubscribers() {
		return hasSubscribers;
	}
	
	public String getResourceURL() {
		if (uri == null) {
			return "rest/ale/" + toString().toLowerCase() + "/specs";
		} else {
			return "rest/ale/" + CC.toString().toLowerCase() + uri;
		}
	}
	
}
