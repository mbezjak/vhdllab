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

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateParameterException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.ParameterNotFoundException;

import java.util.Set;





/**
 * Definira sucelje klasa koje
 * sadrze razne parametre vezane
 * uz zicu (signal).
 * U principu bi ovo mogla biti jedna name-value
 * kolekcija, gdje je name kljuc za dohvat nekog
 * parametra, a value je njegova vrijednost.
 * 
 * Detaljnije u opisu svake funkcije zasebno.
 * 
 * @author Axel
 *
 */
public interface IParameterCollection extends Iterable<IParameter> {
	
	/**
	 * Vraca IParameter objekt za
	 * navedeni kljuc.
	 * Preko IParameter objekta je
	 * moguce dohvatiti i vrijednost
	 * i tip parametra.
	 * 
	 * @param key
	 * Kljuc za dohvat.
	 * @throws ParameterNotFoundException
	 * Navedenu iznimku ako parametar ne postoji.
	 * @return
	 * IParametar ako isti postoji.
	 */
	IParameter getParameter(String key) throws ParameterNotFoundException;
	
	
	/**
	 * Dodaje parametar u kolekciju.
	 * 
	 * @param parameter
	 * Sam parametar. Za kljuc se uzima ime parametra.
	 * @throws DuplicateParameterException
	 * Ako parametar vec postoji pod tim kljucem.
	 */
	void addParameter(IParameter parameter) throws DuplicateParameterException;
	
	
	/**
	 * Mice parametar iz kolekcije parametara.
	 * 
	 * @param key
	 * Kljuc pod kojim je spremljen parametar.
	 * @throws ParameterNotFoundException
	 * Ako parametar ne postoji, baca se ova iznimka.
	 * 
	 */
	void removeParameter(String key) throws ParameterNotFoundException;
	
	
	/**
	 * Vraca vrijednost parametra za navedeni kljuc.
	 * Pritom nije moguce odrediti tip parametra,
	 * osim ako je on unaprijed poznat.
	 * Ova se funkcija moze pozvati izravno nakon
	 * getParameter() od dobivenog parametra, pa
	 * je ona u biti pokrata.
	 * 
	 * @param key
	 * Kljuc za dohvat.
	 * @throws ParameterNotFoundException
	 * Navedenu iznimku ako parametar ne postoji.
	 * @return
	 * IParametar ako isti postoji.
	 * 
	 */
	Object getValue(String key) throws ParameterNotFoundException;
	
	/**
	 * Sluzi za postavljanje neke vrijednosti nekog
	 * parametra odredenog sa key.
	 * Ako parametar ne postoji, bit ce stvoren
	 * novi parametar.
	 * Ova se funkcija moze pozvati izravno nakon
	 * getParameter() od dobivenog parametra, pa
	 * je ona u biti pokrata.
	 * 
	 * @param key
	 * Kljuc.
	 * @param value
	 * Vrijednost.
	 * @throws ParameterNotFoundException
	 * Ukoliko parametar ne postoji.
	 * @throws InvalidParameterValueException
	 * Iznimka je propagirana od parametra kojem je postavljena
	 * vrijednost, a baca se u slucaju da vrijednost
	 * koju zelimo postaviti nije primjenjiva.
	 * 
	 */
	void setValue(String key, Object value) throws ParameterNotFoundException, InvalidParameterValueException;
	
	/**
	 * Dohvaca broj parametara u kolekciji
	 * parametara.
	 * Pritom je povratna vrijednost nenegativna,
	 * no vraca se int radi jednostavnosti pisanja
	 * for petlji :)
	 */
	int count();
	
	/**
	 * Vraca listu svih imena parametara.
	 * 
	 */
	Set<String> getParameterNames();
	
	
	/**
	 * Brise sve parametre u kolekciji.
	 *
	 */
	void clear();
}







