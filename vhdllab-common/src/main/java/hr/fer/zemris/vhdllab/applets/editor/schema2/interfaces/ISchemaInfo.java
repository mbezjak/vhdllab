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

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;

import java.util.Map;



/**
 * Definira izgled wrappera za
 * podatke relevantne schematicu. 
 * Sluzi za pristup svim podacima
 * unutar sheme - komponentama, zicama,
 * informaciji o broju sklopova, itd.
 * 
 * @author Axel
 *
 */
public interface ISchemaInfo {
	
	/**
	 * Vraca objekt koji sadrzi sve komponente.
	 * 
	 */
	ISchemaComponentCollection getComponents();
	
	
	/**
	 * Postavlja objekt koji sadrzi sve komponente.
	 * 
	 * @param components
	 */
	void setComponents(ISchemaComponentCollection components);
	
	
	/**
	 * Vraca objekt koji sadrzi sve zice (signale).
	 * 
	 */
	ISchemaWireCollection getWires();
	
	
	/**
	 * Postavlja objekt koji sadrzi sve zice.
	 * 
	 * @param wires
	 */
	void setWires(ISchemaWireCollection wires);
	
	
	/**
	 * Vraca objekt koji sadrzi CircuitInterface
	 * modeliranog objekta, i jos neke dodatne
	 * podatke, npr. parametre generic bloka.
	 * 
	 */
	ISchemaEntity getEntity();
	
	/**
	 * Vraca listu prototipova u shemi.
	 * 
	 */
	Map<Caseless, ISchemaComponent> getPrototypes();
	
	/**
	 * Vraca objekt s kojim je moguce instancirati
	 * nove komponente.
	 * 
	 */
	ISchemaPrototypeCollection getPrototyper();
	
	/**
	 * Vraca identifikator koji dosad nije koristen u schemi.
	 * 
	 */
	Caseless getFreeIdentifier();
	
	/**
	 * Vraca identifikator koji dosad nije koristen u schemi.
	 * 
	 * @param offered
	 * Ponudeni identifikator.
	 * @return
	 * Ako ponudeni identifikator nije koristen, tada je on
	 * povratna vrijednost metode (ne kopija).
	 * Inace se vraca identifikator koji nije koristen u schemi,
	 * ali je 'slican' ponudenom (slican => ovisno o implementaciji).
	 * 
	 */
	Caseless getFreeIdentifier(Caseless offered);
	
	/**
	 * Provjerava da li je identifikator koristen u schemi.
	 * 
	 * @param id
	 */
	boolean isFreeIdentifier(Caseless identifier);
	
	/**
	 * Vraca bounding box unutar kojeg stanu sve komponente i zice na shemi.
	 */
	Rect2d boundingBox();
	
}














