package havis.net.ui.middleware.client.shared.report.model;

public enum ReportSet {
	ADDITIONS("ADDITIONS"),
	CURRENT("CURRENT"),
	DELETIONS("DELETIONS");
	
	/**
	 * {@link name} representation
	 */
	private String name;

	private ReportSet(String name) {
		this.name = name;
	}

	/**
	 * Return the {@link #name}
	 * 
	 * @return {@link #name}

	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the corresponding {@link #name}
	 * 
	 * @param name
	 * @return the corresponding {@link #name}
	 */
	public static ReportSet getName(String name) {
		for (ReportSet n : ReportSet.values()) {
			if (n.name.equals(name))
				return n;
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}

