package havis.net.ui.middleware.client.cc.cmd.operation;

/**
 * See 9.3.8 of ALE Specification, Version 1.1.1
 */
public enum CCLockOperation {
	UNLOCK("UNLOCK"),
	PERMAUNLOCK("PERMAUNLOCK"),
	LOCK("LOCK"),
	PERMALOCK("PERMALOCK");

	private String ccLockOperation;

	private CCLockOperation(String ccLockOperation) {
		this.ccLockOperation = ccLockOperation;
	}

	public static CCLockOperation getCCLockOperation(String ccLockOperation) {
		for (CCLockOperation c : CCLockOperation.values()) {
			if (c.ccLockOperation.equals(ccLockOperation))
				return c;
		}
		return null;
	}

	public String getCCLockOperation() {
		return ccLockOperation;
	}

	public String toString() {
		return ccLockOperation;
	}

}