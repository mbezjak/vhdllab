package hr.fer.zemris.vhdllab.string;


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
	 * Checks to see if string is alpha-numeric. if
	 * <code>s</code> is empty string this method will return
	 * <code>true</code>.
	 * 
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is
	 * 			alpha-numeric; <code>false</code> otherwise;
	 */
	public static boolean isAlphaNumeric(String s) {
		for(char c : s.toCharArray()) {
			if(!isAlphaNumeric(c)) return false;
		}
		return true;
	}
	
	/**
	 * Checks to see if character is alpha-numeric.
	 * 
	 * @param c a character that will be checked.
	 * @return <code>true</code> if <code>c</code> is
	 * 			alpha-numeric; <code>false</code> otherwise;
	 */
	public static boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isNumeric(c);
	}
	
	/**
	 * Check if <code>c</code> is an alpha character.
	 * (letter of english alphabet, a-z or A-Z)
	 *  
	 * @param c a character that will be checked.
	 * @return <code>true</code> if <code>c</code> is an alpha character; <code>false</code> otherwise.
	 */
	public static boolean isAlpha(char c) {
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
	public static boolean isNumeric(char c) {
		if( (c >= '0' && c <= '9') ) return true;
		return false;
	}
	
	/**
	 * Check if <code>c</code> is an underscore character. (character '_')
	 * 
	 * @param c a character that will be checked.
	 * @return <code>true</code> is <code>c</code> is an underscore character; <code>false</code> otherwise.
	 */
	public static boolean isUnderscore(char c) {
		if( c == '_' ) return true;
		return false;
	}
}
