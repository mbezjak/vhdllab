package hr.fer.zemris.vhdllab.applets.schema2.model.parameters.events;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Event koji nakon promjene imena zice ili komponente automatski mijenja kljuc
 * pod kojim je komponenta ili zica spremljena u kolekciju. Kako akcija ovog
 * eventa ovisi iskljucivo o vrijednosti imena komponente, a ne o njegovom
 * stanju ili stanju shematica, ovaj je event undoable.
 * 
 * @author Axel
 * 
 */
public class NameChanger implements IParameterEvent {

	/* static fields */

	
	/* private fields */
	
	

	/* ctors */

	public NameChanger() {
	}


	
	/* methods */

	public List<ChangeTuple> getChanges() {
		List<ChangeTuple> changes = new ArrayList<ChangeTuple>();

		changes.add(new ChangeTuple(EPropertyChange.PROPERTY_CHANGE));

		return changes;
	}

	public boolean isUndoable() {
		return true;
	}

	public boolean performChange(Object oldvalue, IParameter parameter,
			ISchemaInfo info, ISchemaWire wire, ISchemaComponent component)
	{
		Caseless oldkey = new Caseless((Caseless) oldvalue);

		if (wire != null) {
			return performWireNameChange(oldkey, info, wire, parameter);
		} else if (component != null) {
			return performComponentNameChange(oldkey, info, component, parameter);
		}
		
		return true;
	}

	private boolean performWireNameChange(Caseless oldname, ISchemaInfo info,
			ISchemaWire wire, IParameter parameter)
	{
		ISchemaWireCollection wires = info.getWires();
		
		if (wires.containsName(wire.getName())) return false;
		
		try {
			wires.removeWire(oldname);
		} catch (UnknownKeyException e) {
			return false;
		}
		
		try {
			wires.addWire(wire);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Wire could not be placed " +
					"back to the same location.");
		} catch (OverlapException e) {
			throw new IllegalStateException("Wire could not be placed " +
					"back to the same location.");
		}
		
		// unplug old wire from this one, and plug new one to components
		Caseless newname = wire.getName();
		for (PlacedComponent placed : info.getComponents()) {
			for (SchemaPort sp : placed.comp.getSchemaPorts()) {
				if (sp.getMapping().equals(oldname)) sp.setMapping(newname);
			}
		}
		
		return true;
	}

	private boolean performComponentNameChange(Caseless oldname, ISchemaInfo info,
			ISchemaComponent cmp, IParameter parameter)
	{
		ISchemaComponentCollection components = info.getComponents();
		
		if (components.containsName(cmp.getName())) return false;
		
		XYLocation loc = components.getComponentLocation(oldname);
		
		if (loc == null) return false;
		
		try {
			components.removeComponent(oldname);
		} catch (UnknownKeyException e) {
			return false;
		}
		
		try {
			components.addComponent(loc.x, loc.y, cmp);
		} catch (DuplicateKeyException e) {
			throw new IllegalStateException("Component could not be placed " +
				"again on the same location.");
		} catch (OverlapException e) {
			throw new IllegalStateException("Component could not be placed " +
					"again on the same location.");
		}
		
		return true;
	}
}
















