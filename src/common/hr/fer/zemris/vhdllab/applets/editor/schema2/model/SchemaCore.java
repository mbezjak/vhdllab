package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultType;
import hr.fer.zemris.vhdllab.vhdl.model.Direction;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

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
		addInOutComponent(new DefaultPort("input_v", Direction.IN, new DefaultType("std_logic_vector",
				new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		addInOutComponent(new DefaultPort("output_v", Direction.OUT, new DefaultType("std_logic_vector",
				new int[] {0, 3}, DefaultType.VECTOR_DIRECTION_TO)));
		addInOutComponent(new DefaultPort("input_s", Direction.IN, new DefaultType("std_logic",
				DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
		addInOutComponent(new DefaultPort("output_s", Direction.OUT, new DefaultType("std_logic",
				DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION)));
	}
	
	private void addInOutComponent(Port p) {
		try {
			prototyper.addPrototype(new InOutSchemaComponent(p));
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
	}

	

}


















