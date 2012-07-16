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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;




/**
 * Brise jedan segment odredene zice.
 * Nece podijeliti zicu na dvije zice ako su
 * brisanjem segmenta nastale dvije cjeline,
 * i nece obrisati zicu ako je obrisan posljednji
 * segment.
 * 
 * @author brijest
 *
 */
public class DeleteSegmentCommand implements ICommand {
	
	/* static fields */
	public static final String COMMAND_NAME = "DeleteSegmentCommand";

	
	
	/* private fields */
	private Caseless wirename;
	private WireSegment segment;
	
	
	
	/* ctors */
	
	/**
	 * Stvara komandu koja brise jedan segment zice.
	 * Pritom je nuzno specificirati ime zice i sam segment
	 * koji se zeli obrisati.
	 * 
	 * @param wireToBeAltered
	 * Ime zice na shemi koju se zeli obrisati.
	 * @param segmentToDelete
	 * Segment zice koji treba obrisati.
	 */
	public DeleteSegmentCommand(Caseless wireToBeAltered, WireSegment segmentToDelete) {
		wirename = wireToBeAltered;
		segment = segmentToDelete;
	}
	
	
	
	/* methods */

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		if (!wire.removeSegment(segment)) {
			return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_SEGMENT,
					"'" + segment.toString() + "' not among segments belonging to wire '" + wirename + "'."));
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire '" + wirename + "' does not exist."));
		
		wire.insertSegment(segment);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
	
}

















