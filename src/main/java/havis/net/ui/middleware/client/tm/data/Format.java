package havis.net.ui.middleware.client.tm.data;

import java.util.ArrayList;
import java.util.List;

/**
 * See 6.2 of ALE Specification, Version 1.1.1
 */
public enum Format {
	/**
	 * See 6.2.1.2 of ALE Specification, Version 1.1.1
	 */
	EPC_PURE("epc-pure"),
	/**
	 * See 6.2.1.2 of ALE Specification, Version 1.1.1
	 */
	EPC_TAG("epc-tag"),
	/**
	 * See 6.2.1.2 of ALE Specification, Version 1.1.1
	 */
	EPC_HEX("epc-hex"),
	/**
	 * See 6.2.1.2 of ALE Specification, Version 1.1.1
	 */
	EPC_DECIMAL("epc-decimal"),
	/**
	 * See 6.2.2.2 of ALE Specification, Version 1.1.1<br>
	 * and<br>
	 * See 6.2.3.2 of ALE Specification, Version 1.1.1
	 */
	HEX("hex"),
	/**
	 * See 6.2.2.2 of ALE Specification, Version 1.1.1
	 */
	DECIMAL("decimal"),
	/**
	 * See 6.2.4.1 of ALE Specification, Version 1.1.1
	 */
	STRING("string");

	/**
	 * The format
	 */
	private String format;

	private Format(String format) {
		this.format = format;
	}

	/**
	 * Returns the {@link #format}
	 * 
	 * @return {@link #format}
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Returns the corresponding {@link Format}
	 * 
	 * @param format
	 * @return the corresponding {@link Format}
	 */
	public static Format getFormat(String format) {
		for (Format f : Format.values()) {
			if (f.format.equals(format))
				return f;
		}
		return null;
	}

	@Override
	public String toString() {
		return format;
	}
	
	public static List<String> asStringList() {
		ArrayList<String> result = new ArrayList<>();
		for (Format f : Format.values()) {
			result.add(f.toString());
		}
		return result;
	}
}
