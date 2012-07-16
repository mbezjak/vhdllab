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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import java.util.HashMap;
import java.util.Map;




public class ShortcutTable {
	
	/* static fields */
	public static final String SPLIT_SYMBOL = "=";

	/* private fields */
	private Map<String, String> shortcutmap;
	
	
	/* ctors */

	public ShortcutTable() {
		shortcutmap = new HashMap<String, String>();
	}
	
	
	
	/* methods */

	/**
	 * @param shortcut
	 * Shortcut must be in the following form:
	 * "key=value"
	 * Thus equal sign cannot (and need not) be
	 * a part of the key.
	 */
	public void add(String shortcut) {
		String[] split = shortcut.split(SPLIT_SYMBOL, 2);
		if (split.length != 2) {
			throw new IllegalArgumentException("Error with '" + shortcut +
					"'. Shortcut must be 'key=value'.");
		}
		shortcutmap.put(split[0], split[1]);
	}
	
	/**
	 * 
	 * @param key
	 */
	public String get(String key) {
		return shortcutmap.get(key);
	}
	
}
















