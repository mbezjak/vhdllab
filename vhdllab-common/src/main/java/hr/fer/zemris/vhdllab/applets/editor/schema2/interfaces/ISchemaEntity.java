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
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.List;



/**
 * Definira sucelje klasa koje ce sadrzavati
 * opis entity bloka komponente koja se 
 * modelira u schematicu.
 * 
 * @author Axel
 *
 */
public interface ISchemaEntity {
	
	public static final String KEY_NAME = "Name";
	
	
	/**
	 * Vraca sucelje sklopa koji se modelira
	 * u schematicu - broj i vrstu portova.
	 * 
	 */
	CircuitInterface getCircuitInterface(ISchemaInfo info);
	
	
	/**
	 * Vraca mapu parametara komponente
	 * koja se modelira u schematicu.
	 * Na temelju dijela ove kolekcije biti ce kasnije
	 * izgraden generic blok pri generiranju
	 * strukturnog VHDLa.
	 * 
	 */
	IParameterCollection getParameters();
	
	/**
	 * Postavlja parametre (vidi metodu
	 * getParameters()).
	 * 
	 * @param parameters
	 */
	void setParameters(IParameterCollection parameters);
	
	/**
	 * Vraca vrijednost uvijek prisutnog parametra pod kljucem KEY_NAME,
	 * koji oznacava ime entity-a.
	 */
	Caseless getName();
	
	/**
	 * Dohvaca portove sucelja modelirane komponente.
	 * 
	 */
	public List<Port> getPorts(ISchemaInfo info);
	
	/**
	 * Brise portove i ne-defaultne parametre.
	 *
	 */
	void reset();
	
}








