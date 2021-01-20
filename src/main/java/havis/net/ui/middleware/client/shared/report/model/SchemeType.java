package havis.net.ui.middleware.client.shared.report.model;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public enum SchemeType {
	SGTIN("sgtin","Serialized Global Trade Item Number (SGTIN)", 0),
	SSCC("sscc","Serial Shipping Container Code (SSCC)", 1),
	SGLN("sgln","Global Location Number (SGLN)", 2),
	GRAI("grai","Global Returnable Asset Identifier (GRAI)", 3),
	GIAI("giai","Global Individual Asset Identifier (GIAI)", 4),
	GSRN("gsrn","Global Service Relation Number (GSRN)", 5),
	GDTI("gdti","Global Document Type Identifier (GDTI)", 6),
	GID("gid","General Identifier (GID)", 7);

	private final static String pattern = "([a-z]+)-([0-9]+)";
	
	private String name;
	private String description;
	private int index;
	private static Integer length;

	private SchemeType(String name, String description, Integer index) {
		this.name = name;
		this.description = description;
		this.index = index;
		calcLength(name);
	}
	
	public static SchemeType getValue(String name) {
		for (SchemeType s : SchemeType.values()) {
			if (name.startsWith(s.name)) {
				calcLength(name);
				return s;
			}
		}
		return null;
	}
	
	
	
	public static SchemeType getValueOfIndex(int index) {
		for (SchemeType s : SchemeType.values()) {
			if (s.index == index) {
				return s;
			}
		}
		return null;
	}
	
	
	public static int getIndex(String name) {
		for (SchemeType s : SchemeType.values()) {
			if (name.startsWith(s.name)) {
				calcLength(name);
				return s.index;
			}
		}
		return -1;
	}
	
	public static String getDescription(String name) {
		for (SchemeType s : SchemeType.values()) {
			if (name.startsWith(s.name)) {
				calcLength(name);
				return s.description;
			}
		}
		return null;
	}
	
	private static void calcLength(String scheme){
		RegExp regExp = RegExp.compile(pattern);
		MatchResult result = regExp.exec(scheme);
		length = null;
		if (result != null) {
			if(result.getGroupCount() > 2)
				length = new Integer(result.getGroup(2));
		}
	}
	

	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public Integer getLength() {
		return length;
	}
	
	@Override
	public String toString() {
		return description;
	}

}
