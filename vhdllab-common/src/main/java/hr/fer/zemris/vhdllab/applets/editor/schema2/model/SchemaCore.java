package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;

/**
 * 
 * @author Axel
 * 
 */
public class SchemaCore implements ISchemaCore {
	private ISchemaInfo info;
	
	
	

	public SchemaCore() {
		info = new SchemaInfo();
	}

	public ISchemaInfo getSchemaInfo() {
		return info;
	}

	public void setSchemaInfo(ISchemaInfo nfo) {
		info = nfo;
	}

	public ICommandResponse executeCommand(ICommand command) {
		return command.performCommand(info);
	}
	
	@Override
	public ICommandResponse undoCommand(ICommand comm) throws InvalidCommandOperationException {
	    return comm.undoCommand(info);
	}

	public void reset() {
		info.getComponents().clear();
		info.getWires().clear();
		info.getEntity().reset();
	}

	private ISchemaPrototypeCollection prototyper = null;
	public void initPrototypes(String serializedPrototypes) {		
		// deserialize prototypes from given list
		PredefinedComponentsParser predefparser = 
			new PredefinedComponentsParser(serializedPrototypes);
		PredefinedConf predefconf = predefparser.getConfiguration();

		prototyper = info.getPrototyper();
		prototyper.clearPrototypes();
		for (PredefinedComponent pc : predefconf.getComponents()) {
			try {
				prototyper.addPrototype(new DefaultSchemaComponent(pc));
			} catch (DuplicateKeyException e) {
				throw new SchemaException("Duplicate component.", e);
			}
		}
		
		// add InOutSchemaComponents to list - this is unique for this implementation
		addInOutComponent(new Port("input_v", PortDirection.IN, 0, 3));
		addInOutComponent(new Port("output_v", PortDirection.OUT, 0, 3));
		addInOutComponent(new Port("input_s", PortDirection.IN));
		addInOutComponent(new Port("output_s", PortDirection.OUT));
	}
	
	private void addInOutComponent(Port p) {
		try {
			prototyper.addPrototype(new InOutSchemaComponent(p));
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
	}

	

}


















