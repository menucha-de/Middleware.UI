package havis.net.ui.middleware.client.shared.report.model;

public enum FieldType {
	PRE_DEFINED("Pre-defined"), USER_DEFINED_FIXED("User-defined-fixed"), USER_DEFINED_VARIABLE("User-defined-variable");
	private String scheme;
	

	private FieldType(String scheme) {
		this.scheme = scheme;
	}

	
	public static FieldType getValue(String scheme) {
		for (FieldType s : FieldType.values()) {
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
