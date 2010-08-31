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
 * 
 * Sucelje koje opisuje tvornice
 * koje stvaraju objekte.
 * 
 * @author Axel
 *
 */
@Deprecated
public interface ISchemaComponentGenerator {
	/**
	 * Kreira jedan objekt tipa
	 * ISchemaComponent na temelju specificiranog
	 * generic bloka i entity bloka.
	 * 
	 * @param entityBlock
	 * Entity blok odreduje koje ce ulaze i izlaze
	 * imati pojedini sklop na shemi.
	 * Takoder, na temelju generic dijela je moguce
	 * izgenerirati IParameterCollection za komponentu.
	 * 
	 * @return
	 * Vraca jedan objekt tipa ISchemaComponent.
	 */
	ISchemaComponent generateSchemaComponent(String entityBlock);
}








