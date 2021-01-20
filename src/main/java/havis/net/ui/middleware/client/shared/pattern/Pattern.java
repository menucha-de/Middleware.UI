package havis.net.ui.middleware.client.shared.pattern;

import com.google.gwt.regexp.shared.RegExp;

public class Pattern {
	public static String PatternSyntax = ".*[\u0021-\u002F\u003A-\u0040\u005B-\u005E\u0060\u007B-\u007E\u00A1-\u00A7\u00A9\u00AB-\u00AC\u00AE\u00B0-\u00B1\u00B6\u00BB\u00BF\u00D7\u00F7\u2010-\u2027\u2030-\u203E\u2041-\u205E\u2190-\u245F\u2500-\u2775\u2694-\u2E7F\u3001-\u3003\u3008-\u3020\u3030\uFD3E-\uFD3F\uFE45-\uFE46].*";
	public static String PatternWhiteSpace = ".*[\\s\u200E-\u200F].*";
	public static String PatternOid = "^urn:oid:\\d+(\\.[0-9A-Z]+)*(\\.\\*)?$";

	/**
	 * Attempts to find the next subsequence of the input sequence that matches
	 * the pattern.
	 * 
	 * @param pattern
	 *            regular expression
	 * @param input
	 *            The character sequence to be matched
	 * @return true if pattern found
	 * @throws RuntimeException
	 *             if the pattern is invalid
	 */
	public static boolean match(String pattern, String input) throws RuntimeException {
		RegExp regex = RegExp.compile(pattern);
		return regex.exec(input) == null ? false : true;
	}
}
