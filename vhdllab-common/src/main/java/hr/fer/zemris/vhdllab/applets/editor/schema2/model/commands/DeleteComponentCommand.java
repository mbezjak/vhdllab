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
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;

import java.awt.Rectangle;







public class DeleteComponentCommand implements ICommand {
	
	/* static fields */
	public static final String COMMAND_NAME = "DeleteComponentCommand";
	
	
	/* private fields */
	private Caseless cmpname;
	private int x, y;
	private ISchemaComponent deleted;
	private int deletedIndex;

	
	/* ctors */

	public DeleteComponentCommand(Caseless componentName) {
		cmpname = componentName;
		deleted = null;
		x = 0;
		y = 0;
	}
	
	
	
	/* methods */

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return true;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ICommandResponse response;
		
		try {
			// cache deleted component (for undo)
			cacheComponent(info);
			
			// remove component
			info.getComponents().removeComponent(cmpname);
			
			response = new CommandResponse(new ChangeTuple(EPropertyChange.ANY_CHANGE));
		} catch (UnknownKeyException e) {
			response = new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME,
					"Component with name '" + cmpname + "' does not exist."));
		}
		
		return response;
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException
	{
		if (deleted == null) 
			return new CommandResponse(new SchemaError(EErrorTypes.CANNOT_UNDO,
					"Deleted component does not seem to be cached."));
		
		// restore previous state
		try {
			info.getComponents().addComponentAt(x, y, deleted, deletedIndex);
		} catch (DuplicateKeyException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.DUPLICATE_COMPONENT_NAME,
			"Component with name '" + deleted.getName() + "' already exists."));
		} catch (OverlapException e) {
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_OVERLAP,
				"Component would overlap at (" + x + ", " + y + ")."));
		}
		
		deleted = null;
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
	private void cacheComponent(ISchemaInfo info) throws UnknownKeyException {
		ISchemaComponentCollection components = info.getComponents();
		deletedIndex = components.getComponentIndex(cmpname);
		deleted = components.fetchComponent(cmpname);
		if (deleted == null) throw new UnknownKeyException();
		Rectangle r = components.getComponentBounds(cmpname);
		x = r.x;
		y = r.y;
	}
	
}























