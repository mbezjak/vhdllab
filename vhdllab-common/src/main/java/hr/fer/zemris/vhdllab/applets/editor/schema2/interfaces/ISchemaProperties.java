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
package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;




/**
 * Bit ce obrisano.
 * 
 * @author brijest
 *
 */
public interface ISchemaProperties {
	
	/**
	 * Dohvaca vrijednost.
	 * 
	 * @param key
	 * Kljuc propertya.
	 * @return
	 * Null ako ne postoji kljuc
	 * za taj property.
	 */
	Object get(String key);

	/**
	 * Dohvaca vrijednost u
	 * Integer tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Integer, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Integer getAsInteger(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * String tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije String, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	String getAsString(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * Double tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Double, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Double getAsDouble(String key) throws ClassCastException;
	
	/**
	 * Dohvaca vrijednost u
	 * Boolean tipu, ako je ona 
	 * tog tipa.
	 * 
	 * @param key
	 * @return
	 * Null ako ne postoji taj kljuc.
	 * @throws ClassCastException
	 * Ako vrijednost pod tim kljucem
	 * nije Boolean, nece biti obavljane
	 * nikakve pretvorbe.
	 */
	Boolean getAsBoolean(String key) throws ClassCastException;
	
	
	/**
	 * Postavlja property pod tim kljucem na
	 * neku vrijednost. Ako je vec nesto postojalo
	 * pod tim kljucem, bit ce pregazeno.
	 * @param key
	 * @param obj
	 */
	void set(String key, Object obj);
	
	
}




