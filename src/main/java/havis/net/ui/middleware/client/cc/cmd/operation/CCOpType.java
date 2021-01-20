package havis.net.ui.middleware.client.cc.cmd.operation;

/**
 * See 9.3.5 of ALE Specification, Version 1.1.1
 */
public enum CCOpType {
	READ("READ"),
	CHECK("CHECK"),
	INITIALIZE("INITIALIZE"),
	ADD("ADD"),
	WRITE("WRITE"),
	DELETE("DELETE"),
	PASSWORD("PASSWORD"),
	KILL("KILL"),
	LOCK("LOCK"),
	/**
	 * See Vendor Specification
	 */
	CUSTOM("CUSTOM");

	private String ccOpType;

	private CCOpType(String name) {
		this.ccOpType = name;
	}

	public static CCOpType getCCOpType(String ccOpType) {
		for (CCOpType c : CCOpType.values()) {
			if (c.ccOpType.equals(ccOpType))
				return c;
		}
		return null;
	}

	public String getCCOpType() {
		return ccOpType;
	}

	@Override
	public String toString() {
		return ccOpType;
	}

}
