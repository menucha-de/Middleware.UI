package havis.net.ui.middleware.client.shared.report.model;

public enum PatternType {
	EPC_PURE("epc-pure","EPC Pure", 0), EPC_TAG("epc-tag", "EPC Tag", 1),
	INT_DEC("decimal","Integer (decimal)", 2), INT_HEX("hex","Integer (hexadecimal)", 3);

	private String scheme;
	private String displayString;
	private int index;

	private PatternType(String scheme, String displayString, int index) {
		this.displayString = displayString;
		this.scheme = scheme;
		this.index = index;
	}

	
	public static String getDisplayString(String scheme) {
		for (PatternType s : PatternType.values()) {
			if (s.scheme.equals(scheme)) {
				return s.displayString;
			}
		}
		return null;
	}
	
	public static PatternType getValue(String scheme) {
		for (PatternType s : PatternType.values()) {
			if (s.scheme.equals(scheme)) {
				return s;
			}
		}
		return null;
	}
	
	public static int getIndex(String scheme) {
		for (PatternType s : PatternType.values()) {
			if (s.scheme.equals(scheme)) {
				return s.index;
			}
		}
		return -1;
	}
	
	public static PatternType getValueOfIndex(int index){
		for (PatternType pt : PatternType.values()) {
			if (pt.index == index) {
				return pt;
			}
		}
		return null;
	}
	

	@Override
	public String toString() {
		return displayString;
	}
	
	public String getScheme() {
		return scheme;
	}
}
