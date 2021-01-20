package havis.net.ui.middleware.client.tm.data;

import java.util.Arrays;
import java.util.List;

/**
 * See 6.2 of ALE Specification, Version 1.1.1
 */
public enum DataType {
	/**
	 * See 6.2.1 of ALE Specification, Version 1.1.1
	 */
	EPC("epc", 
			new Syntax(Format.EPC_PURE, true, true),
			new Syntax(Format.EPC_TAG, true, true),
			new Syntax(Format.EPC_HEX, false, false),
			new Syntax(Format.EPC_DECIMAL, false, false)),
	/**
	 * See 6.2.2 of ALE Specification, Version 1.1.1
	 */
	UINT("uint", 
			new Syntax(Format.HEX, true, true), 
			new Syntax(Format.DECIMAL, true, true)),
	/**
	 * See 6.2.3 of ALE Specification, Version 1.1.1
	 */
	BITS("bits", 
			new Syntax(Format.HEX, false, false)),
	/**
	 * See 6.2.4 of ALE Specification, Version 1.1.1
	 */
	ISO_15962_STRING("iso-15962-string", 
			new Syntax(Format.STRING, false, false));

	/**
	 * The datatype
	 */
	private String datatype;

	/**
	 * Contains info about the pattern syntax of each defined format
	 */
	private Syntax[] syntax;

	/**
	 * Valid formats of the datatype
	 */
	private Format[] formats;

	private DataType(String datatype, Syntax... syntax) {
		this.datatype = datatype;
		this.syntax = syntax;
		Format[] formats = new Format[syntax.length];
		for (int i = 0; i < syntax.length; i++) {
			formats[i] = syntax[i].format;
		}
		this.formats = formats;
	}

	/**
	 * Returns the {@link #datatype}
	 * 
	 * @return {@link #datatype}
	 */
	public String getDatatype() {
		return datatype;
	}

	/**
	 * Returns the {@link #formats}
	 * 
	 * @return {@link #formats}
	 */
	public List<Format> getValidFormats() {
		return Arrays.asList(formats);
	}

	/**
	 * 
	 * @param format
	 * @return {@code true} if a pattern syntax is specified for the format
	 */
	public boolean hasPatternSyntax(Format format) {
		for (Syntax s : syntax) {
			if (s.format.equals(format)) {
				return s.hasPatternSyntax;
			}
		}
		return false;
	}
	
	public boolean hasPatternSyntax(){
		for (Syntax s : syntax) {
			if(s.hasPatternSyntax)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param format
	 * @return {@code true} if a grouping pattern syntax is specified for the
	 *         format
	 */
	public boolean hasGroupingPatternSyntax(Format format) {
		for (Syntax s : syntax) {
			if (s.format.equals(format)) {
				return s.hasGroupingPatternSyntax;
			}
		}
		return false;
	}
	
	public boolean hasGroupingPatternSyntax(){
		for (Syntax s : syntax) {
			if(s.hasGroupingPatternSyntax)
				return true;
		}
		return false;
	}

	/**
	 * Returns the corresponding {@link DataType}
	 * 
	 * @param datatype
	 * @return the corresponding {@link DataType}
	 */
	public static DataType getDataType(String datatype) {
		for (DataType d : DataType.values()) {
			if (d.datatype.equals(datatype))
				return d;
		}
		return null;
	}

	@Override
	public String toString() {
		return datatype;
	}

	private static class Syntax {
		Format format;
		boolean hasPatternSyntax;
		boolean hasGroupingPatternSyntax;

		public Syntax(Format format, boolean hasPatternSyntax, boolean hasGroupingPatternSyntax) {
			this.format = format;
			this.hasPatternSyntax = hasPatternSyntax;
			this.hasGroupingPatternSyntax = hasGroupingPatternSyntax;
		}
	}
}
