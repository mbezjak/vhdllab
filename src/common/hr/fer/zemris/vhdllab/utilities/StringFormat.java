package hr.fer.zemris.vhdllab.utilities;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for checking if strings are of right format.
 * 
 * @author Miro Bezjak
 */
public class StringFormat {

	/**
	 * A name of a file that contains such file names.
	 */
	private static final String NOT_VALID_FILE = "NotValidVHDLNames.txt";

	private static final Set<String> notValidVHDLNames;

	static {
		notValidVHDLNames = new HashSet<String>();
		InputStream is = StringFormat.class.getResourceAsStream(NOT_VALID_FILE);
		String data = FileUtil.readFile(is);
		for (String s : data.split("\n")) {
			s = s.trim();
			if (s.equals("") || s.startsWith("#")) {
				continue;
			}
			notValidVHDLNames.add(s.toLowerCase());
		}

	}

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
	 * Ignore case and check if <code>s</code> is a correct vhdl type. The
	 * only allowed type is:
	 * <ul>
	 * <li>std_logic
	 * <li>std_logic_vector
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct vhdl type;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException
	 *             is <code>s</code> is <code>null</code>.
	 */
	public static boolean isCorrectVHDLType(String s) {
		if (s == null) {
			throw new NullPointerException("String can not be null.");
		}
		return s.equalsIgnoreCase("std_logic")
				|| s.equalsIgnoreCase("std_logic_vector");
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
	 * <li>it must not be a reserved word (check at
	 * hr.fer.zemris.vhdllab.utilities.NotValidVHDLNames.txt)
	 * </ul>
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 */
	public static boolean isCorrectEntityName(String s) {
		if (s == null) {
			return false;
		}
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

		if (notValidVHDLNames.contains(s.toLowerCase())) {
			return false;
		}
		return true;
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct port name. A
	 * correct port name format is the same as
	 * {@link #isCorrectEntityName(String)}.
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 */
	public static boolean isCorrectPortName(String s) {
		return isCorrectEntityName(s);
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct file name. A
	 * correct file name format is the same as
	 * {@link #isCorrectEntityName(String)}.
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 */
	public static boolean isCorrectFileName(String s) {
		return isCorrectEntityName(s);
	}

	/**
	 * Ignore case and check if <code>s</code> is a correct project name. A
	 * correct project name format is the same as
	 * {@link #isCorrectFileName(String)}.
	 * 
	 * @param s
	 *            a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name;
	 *         <code>false</code> otherwise.
	 */
	public static boolean isCorrectProjectName(String s) {
		return isCorrectFileName(s);
	}

}