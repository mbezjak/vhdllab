package hr.fer.zemris.vhdllab.applets.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.schema2.model.CommandResponse;

import java.util.List;











/**
 * Mijenja vrijednost parametra.
 * U slucaju da constraint onemogucava promjenu vrijednosti,
 * razlog nemogucnosti promjene vrijednosti nalazi se u info mapi
 * pod kljucem KEY_CONSTRAINT_EXPLANATION u obliku EConstraintExplanation.
 * 
 * @author Axel
 *
 */
public class SetParameterCommand implements ICommand {
	
	public static enum EParameterHolder {
		entity, wire, component
	}
	
	
	/* static fields */
	public static final String COMMAND_NAME = "SetParameterCommand";
	public static final String KEY_CONSTRAINT_EXPLANATION = "CONSTRAINT_EXPL";


	/* private fields */
	private Caseless objname;
	private String paramname;
	private EParameterHolder holder;
	private Object val, oldval;
	private boolean undoable;
	
	
	/* ctors */
	
	/**
	 * Konstruira zahtjev za promjenom parametra.
	 * 
	 * @param objectName
	 * Ime zice ili komponente kojoj pripada parameter. Ako se radi o
	 * parametru samog entitya (modeliranog sklopa), ovo moze biti null.
	 * @param parameterName
	 * Ime parametra kojem treba promijeniti vrijednost.
	 * @param parameterHolder
	 * Enumeracija koja govori o tome kome pripada parametar (komponenti, zici, ...).
	 * @param value
	 * Nova vrijednost parametra.
	 * @param info
	 * ISchemaInfo objekt koji je ovdje kako bi se vec pri samoj konstrukciji
	 * zahtjeva moglo utvrditi da li je parametar undoable.
	 */
	public SetParameterCommand(Caseless objectName, String parameterName,
			EParameterHolder parameterHolder, Object value, ISchemaInfo info)
	{
		objname = objectName;
		paramname = parameterName;
		holder = parameterHolder;
		val = value;
		
		// determine whether this command is undoable
		// on basis of the parameter itself
		IParameter param = fetchParameter(info);
		if (param == null) {
			undoable = false;
		} else {
			if (param.getParameterEvent() == null) undoable = true;
			else undoable = param.getParameterEvent().isUndoable();
		}
	}
	
	
	
	
	/* methods */
	
	private IParameterCollection fetchParameters(ISchemaInfo info) {
		switch (holder) {
		case entity:
			return info.getEntity().getParameters();
		case wire:
			ISchemaWire wire = info.getWires().fetchWire(objname);
			if (wire == null) return null;
			return wire.getParameters();
		case component:
			ISchemaComponent comp = info.getComponents().fetchComponent(objname);
			if (comp == null) return null;
			return comp.getParameters();
		}
		return null;
	}
	
	private IParameter fetchParameter(ISchemaInfo info) {
		IParameterCollection params = fetchParameters(info);
		
		if (params == null) return null;
		try {
			return params.getParameter(paramname);
		} catch (ParameterNotFoundException e) {
			return null;
		}
	}

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return undoable;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		IParameter param = fetchParameter(info);
		
		if (param == null) return new CommandResponse(new SchemaError(EErrorTypes.PARAMETER_NOT_FOUND,
				"No such wire, component or parameter."));
		
		// save old value for undo
		oldval = param.getValue();
		
		// check constraint
		IParameterConstraint constraint = param.getConstraint();
		EConstraintExplanation cttresult = constraint.getExplanation(val);
		
		if (cttresult != EConstraintExplanation.ALLOWED) {
			CommandResponse response = new CommandResponse(
					new SchemaError(EErrorTypes.PARAMETER_CONSTRAINT_BAN, 
							"Parameter value not allowed by constraint."));
			response.getInfoMap().set(KEY_CONSTRAINT_EXPLANATION, cttresult);
			return response;
		}
		
		// change value
		param.setValue(val);
		
		// fire event if it exists
		IParameterEvent pe = param.getParameterEvent();
		List<ChangeTuple> changes = null;
		if (pe != null) {
			changes = pe.getChanges();
			SchemaError error = fireEvent(info, param, pe, oldval);
			if (error != null) {
				// return old value if something went wrong
				param.setValue(oldval);
				return new CommandResponse(error);
			}
		}
		
		if (changes == null)
			return new CommandResponse(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
		else {
			changes.add(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			return new CommandResponse(changes);
		}
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		if (!undoable) throw new InvalidCommandOperationException("Parameter event is not undoable.");
		
		IParameter param = fetchParameter(info);
		
		if (param == null) return new CommandResponse(new SchemaError(EErrorTypes.PARAMETER_NOT_FOUND,
			"No such wire, component or parameter."));
		
		// check constraint
		IParameterConstraint constraint = param.getConstraint();
		EConstraintExplanation cttresult = constraint.getExplanation(oldval);
		
		if (cttresult != EConstraintExplanation.ALLOWED) {
			CommandResponse response = new CommandResponse(
					new SchemaError(EErrorTypes.PARAMETER_CONSTRAINT_BAN, 
							"Parameter value not allowed by constraint."));
			response.getInfoMap().set(KEY_CONSTRAINT_EXPLANATION, cttresult);
			return response;
		}
		
		// change value
		param.setValue(oldval);
		
		// fire event if it exists
		IParameterEvent pe = param.getParameterEvent();
		List<ChangeTuple> changes = null;
		if (pe != null) {
			changes = pe.getChanges();
			SchemaError error = fireEvent(info, param, pe, val);
			if (error != null) {
				// return old value if something went wrong
				param.setValue(val);
				return new CommandResponse(error);
			}
		}
		
		if (changes == null)
			return new CommandResponse(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
		else {
			changes.add(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			return new CommandResponse(changes);
		}
	}
	
	private SchemaError fireEvent(ISchemaInfo info, IParameter param, IParameterEvent pe,
			Object oldvalue) {
		ISchemaComponent component = null;
		ISchemaWire wire = null;
		
		switch (holder) {
		case component:
			component = info.getComponents().fetchComponent(objname);
			if (component == null) return new SchemaError(EErrorTypes.NONEXISTING_COMPONENT_NAME);
			break;
		case wire:
			wire = info.getWires().fetchWire(objname);
			if (wire == null) return new SchemaError(EErrorTypes.NONEXISTING_WIRE_NAME);
			break;
		}
		
		if (!pe.performChange(oldvalue, param, info, wire, component))
			return new SchemaError(EErrorTypes.EVENT_NOT_COMPLETED,
					"Parameter value could not be changed.");
		
		return null;
	}
	
}


















