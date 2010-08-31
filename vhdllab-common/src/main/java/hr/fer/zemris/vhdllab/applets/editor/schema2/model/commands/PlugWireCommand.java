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
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;



public class PlugWireCommand implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = "ConnectWireCommand";
	
	
	/* private fields */
	private Caseless cmpname, wirename, portname;

	
	
	/* ctors */

	public PlugWireCommand(Caseless componentName, Caseless schemaWireName, Caseless schemaPortName) {
		cmpname = componentName;
		wirename = schemaWireName;
		portname = schemaPortName;
	}
	
	
	
	
	/* methods */
	
	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaComponent cmp = info.getComponents().fetchComponent(cmpname);
		
		if (cmp == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME,
				"Component with name '" + cmpname.toString() + "' could not be found."));
		if (cmp.isInvalidated()) return new CommandResponse(
				new SchemaError(EErrorTypes.COMPONENT_INVALIDATED, "Component with name '" + 
						cmpname.toString() + "' is invalidated."));
		
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + wirename.toString() + "' could not be found."));
		
		SchemaPort port = cmp.getSchemaPort(portname);
		
		if (port == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_PORT_NAME,
				"Port '" + portname.toString() + "' could not be found on component '" + cmpname.toString() + "'."));
		
		if (!Caseless.isNullOrEmpty(port.getMapping())) return new CommandResponse(new SchemaError(EErrorTypes.MAPPING_ERROR,
				"Port '" + portname.toString() + "' already mapped to wire '" + port.getMapping().toString() + "'."));
		
		port.setMapping(wire.getName());
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	public ICommandResponse undoCommand(ISchemaInfo info)
			throws InvalidCommandOperationException
	{
		ISchemaComponent cmp = info.getComponents().fetchComponent(cmpname);
		
		if (cmp == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME,
				"Component with name '" + cmpname.toString() + "' could not be found."));
		
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		
		if (wire == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME,
				"Wire with name '" + wirename.toString() + "' could not be found."));
		
		SchemaPort port = cmp.getSchemaPort(portname);
		
		if (port == null) return new CommandResponse(new SchemaError(EErrorTypes.NONEXISTING_PORT_NAME,
				"Port '" + portname.toString() + "' could not be found on component '" + cmpname.toString() + "'."));
		
		if (port.getMapping() == null) return new CommandResponse(new SchemaError(EErrorTypes.MAPPING_ERROR,
				"Port '" + portname.toString() + "' not mapped to anything - cannot undo."));
		
		port.setMapping(null);
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

}










