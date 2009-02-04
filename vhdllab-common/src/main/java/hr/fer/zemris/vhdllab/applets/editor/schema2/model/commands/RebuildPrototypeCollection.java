package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.SchemaException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.DefaultSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.UserComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.PredefinedComponentsParser;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedConf;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.InputStream;
import java.util.List;



/**
 * Nanovo izgraduje kolekciju prototipova.
 * 
 * @author Axel
 *
 */
public class RebuildPrototypeCollection implements ICommand {
	
	/* static fields */
	public static final String COMMAND_NAME = RebuildPrototypeCollection.class.getSimpleName();
	
	/* private fields */
	private InputStream predefinedcircuits;
	private List<CircuitInterface> usercircuits;
	
	
	/* ctors */
	
	
	/**
	 * 
	 * @param predefinedSystemCircuits
	 * Ako je null, lista predefiniranih prototipova
	 * nece biti mijenjana (ni brisana).
	 * @param userDefinedCircuits
	 * Ako je null, korisnicki sklopovi nece biti
	 * mijenjani (ni brisani).
	 */
	public RebuildPrototypeCollection(InputStream predefinedSystemCircuits,
			List<CircuitInterface> userDefinedCircuits)
	{
		predefinedcircuits = predefinedSystemCircuits;
		usercircuits = userDefinedCircuits;
	}
	
	
	/* methods */


	public String getCommandName() {
		return COMMAND_NAME;
	}


	public boolean isUndoable() {
		return false;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		// deserialize prototypes from given list
		ISchemaPrototypeCollection prototyper = info.getPrototyper();
		
		if (predefinedcircuits != null) {
			// remove old predefined
			prototyper.clearPrototypes(EComponentType.PREDEFINED);
			
			// add new predefined
			PredefinedComponentsParser predefparser = 
				new PredefinedComponentsParser(FileUtil.readFile(predefinedcircuits));
			PredefinedConf predefconf = predefparser.getConfiguration();
	
			prototyper.clearPrototypes();
			for (PredefinedComponent pc : predefconf.getComponents()) {
				try {
					prototyper.addPrototype(new DefaultSchemaComponent(pc));
				} catch (DuplicateKeyException e) {
					throw new SchemaException("Duplicate component.", e);
				}
			}
		}
		
		// remove old InOuts
		prototyper.clearPrototypes(EComponentType.IN_OUT);
		
		// add InOutSchemaComponents to list - this is unique for this implementation
		addInOutComponent(new Port("input_v", PortDirection.IN, new Type(TypeName.STD_LOGIC_VECTOR,
				new Range(0, VectorDirection.TO, 3))), prototyper);
		addInOutComponent(new Port("output_v", PortDirection.OUT, new Type(TypeName.STD_LOGIC_VECTOR,
                new Range(0, VectorDirection.TO, 3))), prototyper);
		addInOutComponent(new Port("input_s", PortDirection.IN, new Type(TypeName.STD_LOGIC,
			Range.SCALAR)), prototyper);
		addInOutComponent(new Port("output_s", PortDirection.OUT, new Type(TypeName.STD_LOGIC,
	            Range.SCALAR)), prototyper);
		
		// now create user components
		if (usercircuits != null) {
			// remove old
			prototyper.clearPrototypes(EComponentType.USER_DEFINED);
			
			// add new
			for (CircuitInterface ci : usercircuits) {
				try {
					info.getPrototyper().addPrototype(new UserComponent(ci));
				} catch (DuplicateKeyException e) {
				    e.printStackTrace();
					return new CommandResponse(new SchemaError(EErrorTypes.DUPLICATE_PROTOTYPE,
							"Prototype '" + ci.getName() + "' already exists."));
				}
			}
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.PROTOTYPES_CHANGE));
	}
	
	private void addInOutComponent(Port p, ISchemaPrototypeCollection prototyper) {
		try {
			prototyper.addPrototype(new InOutSchemaComponent(p));
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
	}


	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		throw new InvalidCommandOperationException("Not undoable.");
	}
	
	

}














