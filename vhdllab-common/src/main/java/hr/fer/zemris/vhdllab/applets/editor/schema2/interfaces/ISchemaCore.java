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

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;



/**
 * Ovaj interface propisuje nacin funkcioniranja same logike schematica. Njegove
 * implementacije sadrze informacije o stanju sheme, komponentama koje su na
 * shemi, zicama, itd.
 * 
 * @author Axel
 * 
 */
public interface ISchemaCore {

	/**
	 * Dohvaca podatke o shemi.
	 * Podaci o shemi ne smiju se mijenjati
	 * izravno, vec iskljucivo slanjem ICommand
	 * objekata metodi <code>send</code>, koja se
	 * nalazi u ISchemaController-u.
	 * 
	 */
	ISchemaInfo getSchemaInfo();
	
	/**
	 * Postavlja sve podatke vezane uz shemu.
	 * 
	 * @param info
	 */
	void setSchemaInfo(ISchemaInfo info);

	/**
	 * Obavlja navedenu komandu. Ako je ona uspjesno izvedena, i ako
	 * command.isUndoable() vraca true, stavlja je na stog undo komandi, a stog
	 * redo komandi se brise. Ako command.isUndoable() vraca false, stog
	 * prethodno izvedenih komandi se brise.
	 * 
	 * @return Vraca objekt koji vraca sama komanda pri izvodenju.
	 * 
	 */
	ICommandResponse executeCommand(ICommand command);

	ICommandResponse undoCommand(ICommand comm) throws InvalidCommandOperationException;

    /**
	 * Resetira citavu jezgru schematica
	 * u inicijalno stanje.
	 *
	 */
	void reset();
	
	/**
	 * Iz navedenog stringa deserijalizira skup
	 * prototipova.
	 * Odredene implementacije pritom smiju
	 * dodati i svoje vlastite komponente u skup
	 * prototipova.
	 * 
	 * @param serializedPrototypes
	 */
	void initPrototypes(String serializedPrototypes);
	
	
	
	
}












