package havis.net.ui.middleware.client.tm.data;

public enum TagMemoryBank {
	RESERVED(0, "Reserved"),
	EPC_UII(1, "EPC/UII"),
	TID(2, "TID"),
	USER(3, "User");

	/**
	 * {@link TagMemoryBank} value
	 */
	private int bank;
	/**
	 * {@link TagMemoryBank} meaning
	 */
	private String meaning;

	private TagMemoryBank(int bank, String meaning) {
		this.bank = bank;
		this.meaning = meaning;
	}

	/**
	 * Returns the {@link #bank}
	 * 
	 * @return {@link #bank}
	 */
	public int getBank() {
		return bank;
	}

	/**
	 * Returns the {@link #meaning}
	 * 
	 * @return {@link #meaning}
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * Returns the corresponding {@link TagMemoryBank} or null
	 * 
	 * @param bank
	 * @return the corresponding {@link TagMemoryBank}
	 */
	public static TagMemoryBank getTagMemoryBank(Integer bank) {
		if (bank != null) {
			for (TagMemoryBank t : TagMemoryBank.values()) {
				if (t.bank == bank)
					return t;
			}
		}
		return null;
	}
	
	/**
	 * Returns the corresponding {@link TagMemoryBank}
	 * 
	 * @param meaning
	 * @return the corresponding {@link TagMemoryBank}
	 */
	public static TagMemoryBank getTagMemoryBank(CharSequence meaning) {
		if (meaning != null) {
			for (TagMemoryBank t : TagMemoryBank.values()) {
				if (t.meaning.equals(meaning))
					return t;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return meaning;
	}
}
