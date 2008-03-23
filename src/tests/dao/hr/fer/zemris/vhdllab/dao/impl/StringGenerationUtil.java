package hr.fer.zemris.vhdllab.dao.impl;

public class StringGenerationUtil {

	/**
	 * Generates junk string.
	 * 
	 * @param length
	 *            a length of a string
	 * @return a junk string
	 * @throws IllegalArgumentException
	 *             if <code>length</code> &lt; 0
	 */
	public static String generateJunkString(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("length must be >= 0 but was "
					+ length);
		}
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append("a");
		}
		return sb.toString();
	}

}
