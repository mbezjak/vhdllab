package hr.fer.zemris.vhdllab.vhdl.tb;


/**
 * Helper class for checking if strings are of right format.
 * 
 * @author Miro Bezjak
 */
public class StringUtil {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringUtil() {}
	
	/**
	 * Ignore case and check if <code>s</code> is vector direction.
	 * Vector direction is one of the following:
	 * <ul>
	 * <li>DOWNTO
	 * <li>TO
	 * </ul>
	 * <p>
	 * 
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is vector direction; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>s</code> is <code>null</code>.
	 */
	public static boolean isVectorDirection(String s) {
		if( s == null ) throw new NullPointerException("Vector direction can not be null.");
		if( s.equalsIgnoreCase("DOWNTO") ||
			s.equalsIgnoreCase("TO") ) return true;
		return false;
	}
	
	/**
	 * Check if <code>s</code> is a measure unit. Case is not ignored.
	 * Measure unit is one of the following:
	 * <ul>
	 * <li>fs
	 * <li>ps
	 * <li>ns
	 * <li>us
	 * <li>ms
	 * <li>s
	 * </ul>
	 *
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a measure unit; <code>false</code> otherwise.
	 * @throws NullPointerException if <code>s</code> is <code>null</code>.
	 */
	public static boolean isMeasureUnit(String s) {
		if( s == null ) throw new NullPointerException("Measure unit can not be null.");
		if( "fs".equals(s) ||
			"ps".equals(s) ||
			"ns".equals(s) ||
			"us".equals(s) ||
			"ms".equals(s) ||
			"s".equals(s) ) return true;
		else return false;
	}

	
	/**
	 * Ignore case and check if <code>s</code> is a correct name. Correct name is
	 * a string with the following format:
	 * <ul>
	 * <li>it must contain only alpha (only letters of english alphabet), numeric (digits 0 to 9) or underline (_) characters
	 * <li>it must not start with a non-alpha character
	 * <li>it must not end with an underline character
	 * <li>it must not contain an underline character after an underline character
	 * </ul>
	 * 
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is a correct name; <code>false</code> otherwise.
	 * @throws NullPointerException is <code>s</code> is <code>null</code>.
	 */
	public static boolean isCorrectName(String s) {
		if( s == null ) throw new NullPointerException("String can not be null.");
		char[] chars = s.toCharArray();
		if( chars.length == 0 ) return false;
		if( !StringUtil.isAlpha(chars[0]) ) return false;
		if( StringUtil.isUnderline(chars[s.length()-1]) ) return false;
		for(int i = 0; i < chars.length; i++) {      
			if( StringUtil.isAlpha(chars[i]) ||
				StringUtil.isNumeric(chars[i]) ) continue;
			if( StringUtil.isUnderline(chars[i]) ) {
				if( (i + 1) < chars.length 
					&& StringUtil.isUnderline(chars[i+1]) )
					return false;
				continue;
			}
			return false;
		}  
		return true;
	}
	
	/**
	 * Check if <code>c</code> is an alpha character.
	 * (letter of english alphabet, a-z or A-Z)
	 *  
	 * @param c a character that will be checked.
	 * @return <code>true</code> if <code>c</code> is an alpha character; <code>false</code> otherwise.
	 */
	private static boolean isAlpha(char c) {
		if( (c >= 'a' && c <= 'z') ||
			(c >= 'A' && c <= 'Z') ) return true;
		return false;
	}
	
	/**
	 * Check if <code>c</code> os a numeric character. (digit 0-9)
	 * 
	 * @param c a character that will be checked.
	 * @return <code>true</code> is <code>c</code> is a numeric character; <code>false</code> otherwise.
	 */
	private static boolean isNumeric(char c) {
		if( (c >= '0' && c <= '9') ) return true;
		return false;
	}
	
	/**
	 * Check if <code>c</code> is an underline character. (character '_')
	 * 
	 * @param c a character that will be checked.
	 * @return <code>true</code> is <code>c</code> is an underline character; <code>false</code> otherwise.
	 */
	private static boolean isUnderline(char c) {
		if( c == '_' ) return true;
		return false;
	}
}
