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
package hr.fer.zemris.vhdllab.applets.editor.newtb.numbers;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;

public class Hex {
	/**
	 * Same as parseBinary method, just for hex. Accepts both lowercase and uppercase
	 * @param g
	 * @throws NumberFormatException
	 */
	public static int parseHex(String g) throws NumberFormatException {
		if(g == null || g.length() == 0)
			throw new NumberFormatException(Messages.noNumber);
		g = g.toUpperCase();
		try {
			int ret = 0, mult = 1;
			for (int i = g.length() - 1; i >= 0; i--, mult *= 16)
			{
				char t = g.charAt(i);
				if(t > 'F' || (t < 'A' && !Character.isDigit(t)))
						throw new Exception();

				ret += mult
					* (t >= 'A' ? t - 55
							: t - 48);
			}
			return ret;
		} catch (Exception e) {
			throw new NumberFormatException(Messages.wrongHexFormat);
		}
	}

}
