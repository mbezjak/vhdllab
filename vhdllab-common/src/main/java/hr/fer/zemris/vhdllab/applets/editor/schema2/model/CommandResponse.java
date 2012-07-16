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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.InfoMap;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;

import java.util.ArrayList;
import java.util.List;



public class CommandResponse implements ICommandResponse {
	private SchemaError error;
	private boolean success;
	private InfoMap info;
	private List<ChangeTuple> changes;
	
	/**
	 * Defaultni konstruktor
	 * pretpostavlja da nije doslo do greske
	 * i da je komanda uspjela.
	 */
	public CommandResponse() {
		error = null;
		success = true;
		info = new InfoMap();
		changes = null;
	}
	
	/**
	 * Postavlja se uspjesnost zahtjeva.
	 * Za neuspjesan zahtjev error dobiva
	 * UNKNOWN_TYPE tip.
	 * 
	 * @param isSuccessful
	 */
	public CommandResponse(boolean isSuccessful) {
		success = isSuccessful;
		error = (success) ? (null) : (new SchemaError(EErrorTypes.UNKNOWN_TYPE));
		info = new InfoMap();
		changes = null;
	}
	
	/**
	 * Postavlja se odredeni error.
	 * 
	 * @param responseError
	 * Ako se za error specificira null,
	 * isCommandSuccessful() ce biti true.
	 * Ako se specificira neki error,
	 * onda ce isCommandSuccessful() biti false;
	 */
	public CommandResponse(SchemaError responseError) {
		success = (responseError == null) ? (true) : (false);
		error = responseError;
		info = new InfoMap();
		changes = null;
	}
	
	/**
	 * Buduci da postavlja listu promjena,
	 * automatski pretpostavlja da je zahtjev
	 * bio uspjesan.
	 * 
	 * @param changelist
	 */
	public CommandResponse(List<ChangeTuple> changelist) {
		changes = changelist;
		success = true;
		error = null;
		info = new InfoMap();
	}
	
	/**
	 * Automatski stvara listu promjena i dodaje
	 * u tu listu navedenu promjenu. Sluzi kako
	 * bi olaksao stvaranje responsea u slucaju
	 * samo jedne promjene.
	 * Naravno, pretpostavlja uspjesnost zahtjeva.
	 * 
	 * @param oneTuple
	 */
	public CommandResponse(ChangeTuple oneTuple) {
		changes = new ArrayList<ChangeTuple>();
		changes.add(oneTuple);
		success = true;
		error = null;
		info = new InfoMap();
	}

	public SchemaError getError() {
		return error;
	}

	public InfoMap getInfoMap() {
		return info;
	}

	public boolean isSuccessful() {
		return success;
	}

	public List<ChangeTuple> getPropertyChanges() {
		return changes;
	}

}





