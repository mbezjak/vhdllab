package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;

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

	public void reset() {
		info.getComponents().clear();
		info.getWires().clear();
		info.getEntity().reset();
	}

	

}


















