package havis.net.ui.middleware.client.shared.report.model;

public enum PatternLengthType {
	Length_96("96", 0),
	Length_113("113", 1),
	Length_170("170", 2),
	Length_195("195", 3),
	Length_198("198", 4),
	Length_202("202", 5);

	private String name;
	private Integer index;

	private PatternLengthType(String name, Integer index) {
		this.name = name;
		this.index = index;
	}
	
	public static PatternLengthType getValue(String name) {
		for (PatternLengthType s : PatternLengthType.values()) {
			if (s.name.equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	public static PatternLengthType getValueOfIndex(int index) {
		for (PatternLengthType s : PatternLengthType.values()) {
			if (s.index == index) {
				return s;
			}
		}
		return null;
	}
	
	
	public static int getIndex(String name) {
		for (PatternLengthType s : PatternLengthType.values()) {
			if (s.name.equals(name)) {
				return s.index;
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		return name;
	}
}
