package hr.fer.zemris.vhdllab.applets.editor.newtb.util;

import java.math.BigInteger;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class RadixConverter {
	
	public static String binToOtherString(String binaryValue, int radix) {
		if(binaryValue.length() < 32) {
			return Integer.toString(Integer.parseInt(binaryValue, 2), radix).toUpperCase();
		}

		BigInteger a = new BigInteger(binaryValue, 2);
		return a.toString(radix).toUpperCase();
	}
	
	public static String binToOtherString(String binaryValue, int radix, int minNofBits, boolean hexPrefix) {
		String result = null;		
		if(binaryValue.length() < 32) {
			result = Integer.toString(Integer.parseInt(binaryValue, 2), radix).toUpperCase();
		} else {
			BigInteger a = new BigInteger(binaryValue, 2);
			result = a.toString(radix).toUpperCase();
		}
		
		if(radix == 2) {
			StringBuilder sb = new StringBuilder(result);
			for(int i = result.length(); i < minNofBits; i++) {
				sb.insert(0, '0');
			}
			result = sb.toString();
		}
		if(hexPrefix && radix == 16) {
			result = "0x" + result;
		}
		
		return result;
	}
	
	public static String otherToBinString(String value, int radix) {
		BigInteger a = new BigInteger(value, radix);
		return a.toString(2);
	}
}
