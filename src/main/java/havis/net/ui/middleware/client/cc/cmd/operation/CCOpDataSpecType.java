package havis.net.ui.middleware.client.cc.cmd.operation;

/**
 * See 9.3.6 of ALE Specification, Version 1.1.1<br>
 * and<br>
 * 9.3.7 of ALE Specification, Version 1.1.1
 */
public enum CCOpDataSpecType {
	LITERAL("LITERAL"),
	PARAMETER("PARAMETER"),
	CACHE("CACHE"),
	ASSOCIATION("ASSOCIATION"),
	RANDOM("RANDOM");

	private String specType;

	private CCOpDataSpecType(String specType) {
		this.specType = specType;
	}

	public static CCOpDataSpecType getValue(String specType) {
		for (CCOpDataSpecType s : CCOpDataSpecType.values()) {
			if (s.specType.equals(specType))
				return s;
		}
		return null;
	}

	public String getSpecType() {
		return specType;
	}

	public String toString() {
		return specType;
	}

}
