/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
