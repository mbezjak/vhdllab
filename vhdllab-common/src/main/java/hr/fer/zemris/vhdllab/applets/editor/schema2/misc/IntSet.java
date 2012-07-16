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




public interface IntSet extends IntCollection, Iterable<Integer> {

	
	/**
	 * Puts the specified value into this
	 * hash under the specified key.
	 * 
	 * @param key
	 * Integer key. If the key already exists
	 * in dictionary, then the old value will be
	 * overwritten.
	 * @param value
	 * Value to put under the specified key. It can
	 * also be null.
	 */
	void add(int key);

	/**
	 * Checks for existance of the specified key.
	 * 
	 * @param key
	 * @return
	 * True if the key is in the hashmap, false otherwise.
	 */
	boolean contains(int key);

	/**
	 * Removes the element under the specified key.
	 * 
	 * @param key
	 * The key that specified the element to be removed.
	 * If the key does not exist, nothing will be removed.
	 */
	void remove(int key);

	void setLoadFactor(float hashLoadFactor);

	void reset(int newsize);

}
