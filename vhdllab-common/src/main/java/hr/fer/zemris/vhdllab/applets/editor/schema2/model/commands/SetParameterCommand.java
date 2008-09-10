package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EConstraintExplanation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EErrorTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaError;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;

import java.util.ArrayList;
import java.util.List;











/**
 * Mijenja vrijednost parametra.
 * U slucaju da constraint onemogucava promjenu vrijednosti,
 * razlog nemogucnosti promjene vrijednosti nalazi se u info mapi
 * pod kljucem KEY_CONSTRAINT_EXPLANATION u obliku EConstraintExplanation.
 * U slucaju uspjesne promjene parametra, u info mapi ce se pod kljucem
 * KEY_UPDATED_NAME naci novo ime komponente, odnosno zice.
 * 
 * @author Axel
 *
 */
public class SetParameterCommand implements ICommand {
	
	public static enum EParameterHolder {
		entity, wire, component
	}
	
	private static class PPWrapper {
		public IParameterCollection params;
		public IParameter par;
	}
	
	
	/* static fields */
	public static final String COMMAND_NAME = "SetParameterCommand";
	public static final String KEY_CONSTRAINT_EXPLANATION = "CONSTRAINT_EXPL";
	public static final String KEY_UPDATED_NAME = "UPDATED_NAME";


	/* private fields */
	private Caseless objname;
	private String paramname;
	private EParameterHolder holder;
	private Object val, oldval;
	private boolean undoable;
	private PPWrapper ppw;
	private ISchemaWire tmpwire;
	private ISchemaComponent tmpcmp;
	
	
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
		ppw = new PPWrapper();
		objname = objectName;
		paramname = parameterName;
		holder = parameterHolder;
		val = value;
		
		// determine whether this command is undoable
		// on basis of the parameter itself
		fetchParameter(info);
		IParameter param = ppw.par;
		if (param == null) {
			undoable = false;
		} else {
			if (param.getParameterEvent() == null) undoable = true;
			else undoable = param.getParameterEvent().isUndoable();
		}
	}
	
	
	
	
	/* methods */
	
	private EErrorTypes fetchParameters(ISchemaInfo info) {
		switch (holder) {
		case entity:
			ppw.params = info.getEntity().getParameters();
			break;
		case wire:
			ISchemaWire wire = info.getWires().fetchWire(objname);
			if (wire == null) return EErrorTypes.NONEXISTING_WIRE_NAME;
			ppw.params = wire.getParameters();
			tmpwire = wire;
			break;
		case component:
			ISchemaComponent comp = info.getComponents().fetchComponent(objname);
			if (comp == null) return EErrorTypes.NONEXISTING_COMPONENT_NAME;
			if (comp.isInvalidated()) return EErrorTypes.COMPONENT_INVALIDATED;
			ppw.params = comp.getParameters();
			tmpcmp = comp;
			break;
		}
		return EErrorTypes.NO_ERROR;
	}
	
	private EErrorTypes fetchParameter(ISchemaInfo info) {
		IParameterCollection params = null;
		EErrorTypes err = fetchParameters(info);
		params = ppw.params;
		
		if (params == null) return err;
		try {
			ppw.par = params.getParameter(paramname);
		} catch (ParameterNotFoundException e) {
			return EErrorTypes.PARAMETER_NOT_FOUND;
		}
		
		return EErrorTypes.NO_ERROR;
	}

	public String getCommandName() {
		return COMMAND_NAME;
	}

	public boolean isUndoable() {
		return undoable;
	}

	public ICommandResponse performCommand(ISchemaInfo info) {
		IParameter param = null;
		EErrorTypes err = fetchParameter(info);
		param = ppw.par;
		
		if (err.equals(EErrorTypes.PARAMETER_NOT_FOUND) || 
				err.equals(EErrorTypes.NONEXISTING_COMPONENT_NAME) ||
				err.equals(EErrorTypes.NONEXISTING_WIRE_NAME))
		{
			return new CommandResponse(new SchemaError(err,	"No such wire, component or parameter."));
		}
		if (err.equals(EErrorTypes.COMPONENT_INVALIDATED))
			return new CommandResponse(new SchemaError(EErrorTypes.COMPONENT_INVALIDATED,
				"Component that holds the parameter is invalidated."));
		
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
		
		// add updated name to info map
		Caseless updname = getUpdatedName(info);
		
		if (changes == null) {
			CommandResponse cr = new CommandResponse(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			cr.getInfoMap().set(KEY_UPDATED_NAME, updname);
			return cr;
		} else {
			changes = new ArrayList<ChangeTuple>(changes);
			changes.add(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			CommandResponse cr = new CommandResponse(changes);
			cr.getInfoMap().set(KEY_UPDATED_NAME, updname);
			return cr;
		}
	}

	public ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException {
		if (!undoable) throw new InvalidCommandOperationException("Parameter event is not undoable.");
		
		IParameter param = null;
		fetchParameter(info);
		param = ppw.par;
		
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
		
		// add updated name to info map
		Caseless updname = getUpdatedName(info);
		
		if (changes == null) {
			CommandResponse cr = new CommandResponse(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			cr.getInfoMap().set(KEY_UPDATED_NAME, updname);
			return cr;
		}
		else {
			changes = new ArrayList<ChangeTuple>(changes);
			changes.add(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));
			CommandResponse cr = new CommandResponse(changes);
			cr.getInfoMap().set(KEY_UPDATED_NAME, updname);
			return cr;
		}
	}
	
	private Caseless getUpdatedName(ISchemaInfo info) {
		switch (holder) {
		case entity:
			return info.getEntity().getName();
		case wire:
			return tmpwire.getName();
		case component:
			return tmpcmp.getName();
		}
		throw new IllegalStateException("Holder is enum, but did not end in switch?");
	}
	
	private SchemaError fireEvent(ISchemaInfo info, IParameter param, IParameterEvent pe,
			Object oldvalue)
	{
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


















