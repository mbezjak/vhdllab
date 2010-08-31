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
package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;



/**
 * Opisuje tip komponente.
 * 
 * @author brijest
 *
 */
public enum EComponentType {

	/**
	 * Komponente koje su predefinirane unutar
	 * konfiguracijske datoteke, i uvijek su dostupne.
	 * Mogu sadrzavati generic parametre.
	 */
	PREDEFINED,
	/**
	 * Komponente koje su korisnicki definirane,
	 * a konstruirane na temelju entity bloka.
	 * Dostupne su ako su unutar projekta.
	 * Ne sadrze generic parametre.
	 */
	USER_DEFINED,
	/**
	 * Surogat komponente koje sluze kao ulaz i izlaz
	 * iz modeliranog sklopa.
	 * Informacija da je komponenta IN_OUT bitna je na
	 * vise mjesta, npr. kod serijalizacije, ili kod dinamickog
	 * odredivanja portova.
	 */
	IN_OUT,
	/**
	 * Rezervirano za buduce primjene. Komponenta ovog tipa
	 * nece se smatrati ulazom ili izlazom sklopa koji se
	 * modelira, a moze i ne mora sadrzavati generic parametre.
	 */
	NON_BASIC
	
	
}





