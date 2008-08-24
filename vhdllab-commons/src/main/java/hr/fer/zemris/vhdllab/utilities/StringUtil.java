package hr.fer.zemris.vhdllab.utilities;



/**
 * Helper class for checking if strings are of right format.
 * 
 * @author Miro Bezjak
 */
public final class StringUtil {
	
	/**
	 * Don't let anyone instantiate this class.
	 */
	private StringUtil() {}
	
	/**
	 * Checks to see if string is alpha-numeric. If
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
	 * Checks to see if string is alpha (letters of english
	 * alphabet, a-z or A-Z). If <code>s</code> is empty
	 * string this method will return <code>true</code>.
	 * 
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is
	 * 			alpha; <code>false</code> otherwise;
	 */
	public static boolean isAlpha(String s) {
		for(char c : s.toCharArray()) {
			if(!isAlpha(c)) return false;
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
	public static boolean isAlpha(char c) {
		if( (c >= 'a' && c <= 'z') ||
			(c >= 'A' && c <= 'Z') ) return true;
		return false;
	}
	
	/**
	 * Checks to see if string is numeric. If <code>s</code>
	 * is empty string this method will return <code>true</code>.
	 * 
	 * @param s a string that will be checked.
	 * @return <code>true</code> if <code>s</code> is
	 * 			numeric; <code>false</code> otherwise;
	 */
	public static boolean isNumeric(String s) {
		for(char c : s.toCharArray()) {
			if(!isNumeric(c)) return false;
		}
		return true;
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
	
	/**
	 * Use this method to replace any succession of whitespaces with a single
	 * whitespace sign. Whitespaces include tabs, spaces, CR and LF.
	 * 
	 * @param source a source from where to remove white spaces
	 * @return a new string where multiple white spaces are removed
	 */
	public static String removeWhiteSpaces(String source) {
		if(source==null) {
			throw new NullPointerException("No source given!");
		}
		char[] chs = source.toCharArray();
		int pos = 0;
		int i;
		for(i = 0; i < chs.length-1; i++) {
			chs[pos] = chs[i];
			if(chs[i]==' ' || chs[i]=='\n' || chs[i]=='\t' || chs[i]=='\r') {
				chs[pos] = ' ';
				if(pos==0) pos--;
				char c = chs[i+1];
				if(c==' ' || c=='\n' || c=='\t' || chs[i]=='\r') {
					do {
						i++;
						if(i+1>=chs.length) break;
						c = chs[i+1];
					} while(c==' ' || c=='\n' || c=='\t' || chs[i]=='\r');
				}
				pos++;
				continue;
			}
			pos++;
		}
		if(i<chs.length) {
			i = chs.length-1;
			if(chs[i]!=' ' && chs[i]!='\n' && chs[i]!='\t' || chs[i]=='\r') {
				chs[pos] = chs[i];
				pos++;
			}
		}
		if(pos==0) return new String();
		if(chs[pos-1]==' ') pos--;
		if(pos==0) return new String();
		return new String(chs,0,pos);
	}
	
}
