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
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;


/**
 * Brise odredeni prototip iz kolekcije
 * ako takav postoji.
 * 
 * @author Axel
 *
 */
public class RemovePrototype implements ICommand {
	
	/* static fields */
	public static final String COMMAND_NAME = RemovePrototype.class.getSimpleName();

	/* private fields */
	private Caseless toremovename;
	

	/* ctors */
	
	public RemovePrototype(Caseless toBeRemoved) {
		toremovename = toBeRemoved;
	}
	
	

	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaPrototypeCollection prototyper = info.getPrototyper();
		
		if (prototyper.removePrototype(toremovename))
			return new CommandResponse(new ChangeTuple(EPropertyChange.PROTOTYPES_CHANGE));

		return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_PROTOTYPE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException
	{
		throw new InvalidCommandOperationException();
	}

}














