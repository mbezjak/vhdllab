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
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import java.util.HashMap;
import java.util.Map;


/**
 * Pomocna kolekcijica koja je handy
 * u nekim slucajevima.
 * 
 * @author Axel
 *
 */
public final class InfoMap {
	private Map<String, Object> map;
	
	public InfoMap() {
		map = new HashMap<String, Object>();
	}
	
	
	/**
	 * 
	 * @param key
	 * Kljuc.
	 * 
	 * @return
	 * Objekt ako postoji zapis ili
	 * null ako ne postoji.
	 * Moze vratiti null i ako je
	 * netko pohranio null pod tim kljucem.
	 * 
	 */
	public final Object get(String key) {
		return map.get(key);
	}
	
	/**
	 * Dodaje zapis ili prepisuje postojeci.
	 * 
	 * @param key
	 * Kljuc.
	 * @param object
	 * Objekt.
	 */
	public final void set(String key, Object object) {
		map.put(key, object);
	}
	
}



