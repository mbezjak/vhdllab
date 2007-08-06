package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.utilities.StringUtil;

/**
 * Helper class for checking if strings are of right format.
 * 
 * @author Miro Bezjak
 */
public class StringFormat {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringFormat() {
	}

	/**
	 * Check if <code>s</code> is a measure unit. Case is not ignored. Measure
	 * unit is one of the following:
	 * <ul>
	 * <li>fs
	 * <li>ps
	 * <li>ns
	 * <li>us
	 * <li>ms
	 * <li>s
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a measure unit;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             if <code>s</code> is <code>null</code>.
	 */
	public static boolean isMeasureUnit(String s) {
		if (s == null)
			throw new NullPointerException("Measure unit can not be null.");
		if ("fs".equals(s) || "ps".equals(s) || "ns".equals(s)
				|| "us".equals(s) || "ms".equals(s) || "s".equals(s))
			return true;
		else
			return false;
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct entity name.
	 * Correct entity name is a string with the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of English alphabet),
	 * numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain a tandem of underscore characters
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>s</code> is <code>null</code>.
	 */
	public static boolean isCorrectEntityName(String s) {
		if (s == null)
			throw new NullPointerException("String can not be null.");
		char[] chars = s.toCharArray();
		if (chars.length == 0)
			return false;
		if (!StringUtil.isAlpha(chars[0]))
			return false;
		if (StringUtil.isUnderscore(chars[s.length() - 1]))
			return false;
		for (int i = 0; i < chars.length; i++) {
			if (StringUtil.isAlpha(chars[i]) || StringUtil.isNumeric(chars[i]))
				continue;
			if (StringUtil.isUnderscore(chars[i])) {
				if ((i + 1) < chars.length
						&& StringUtil.isUnderscore(chars[i + 1]))
					return false;
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct file name. Correct
	 * file name is a string with the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of English alphabet),
	 * numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain a tandem of underscore characters
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>s</code> is <code>null</code>.
	 */
	public static boolean isCorrectFileName(String s) {
		return isCorrectEntityName(s);
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct project name.
	 * Correct project name is a string with the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of English alphabet),
	 * numeric (digits 0 to 9) or underscore (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underscore character
	 * <li>it must not contain a tandem of underscore characters
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>s</code> is <code>null</code>.
	 */
	public static boolean isCorrectProjectName(String s) {
		return isCorrectEntityName(s);
	}

}