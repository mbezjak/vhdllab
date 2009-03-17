package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.ArrayList;
import java.util.Collections;
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
public class NameAndPortNameChanger implements IParameterEvent {

	/* static fields */
	private static final List<ChangeTuple> changes = new ArrayList<ChangeTuple>();
	private static final List<ChangeTuple> ro_ch = Collections.unmodifiableList(changes);
	static {
		changes.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
	
	/* private fields */
	
	

	/* ctors */

	public NameAndPortNameChanger() {
	}


	
	/* methods */	

	public IParameterEvent copyCtor() {
		return new NameAndPortNameChanger();
	}

	public List<ChangeTuple> getChanges() {
		return ro_ch;
	}

	public boolean isUndoable() {
		return true;
	}

	public boolean performChange(Object oldvalue, IParameter parameter,
			ISchemaInfo info, ISchemaWire wire, ISchemaComponent component)
	{
		if (!(oldvalue instanceof Caseless)) return false;
		
		Caseless oldkey = new Caseless((Caseless) oldvalue);

		if (component != null && (component instanceof InOutSchemaComponent)) {
			return performComponentNameChange(oldkey, info, component, parameter);
		}
		
		return false;
	}

	private boolean performComponentNameChange(Caseless oldname, ISchemaInfo info,
			ISchemaComponent cmp, IParameter parameter)
	{
		ISchemaComponentCollection components = info.getComponents();
		Caseless updatedname = cmp.getName();
		
		/* rename component using the component collection */
		cmp.setName(oldname);
		
		try {
			components.renameComponent(oldname, updatedname);
		} catch (UnknownKeyException e) {
			throw new IllegalStateException("Component was in collection, but cannot be found.");
		} catch (DuplicateKeyException e) {
			/* set updated name back before returning false */
			cmp.setName(updatedname);
			return false;
		}
		
		/* rename component's first port to match component's name */
		InOutSchemaComponent inoutcmp = (InOutSchemaComponent)cmp;
		
		/* first cache old signal mappings */
		List<SchemaPort> oldschemaports = new ArrayList(inoutcmp.getRelatedTo(0));
		
		/* now rename port and rebuild schemaports */
		Port p = inoutcmp.getPort();
		p.setName(updatedname.toString());
		inoutcmp.setPort(p);
		inoutcmp.rebuildSchemaPorts();
		
		/* set back old signal mappings */
		List<SchemaPort> updatedschemaports = inoutcmp.getRelatedTo(0);
		int i = 0;
		for (SchemaPort oldsp : oldschemaports) {
			Caseless mappedto = oldsp.getMapping();
			if (!Caseless.isNullOrEmpty(mappedto)) updatedschemaports.get(i).setMapping(mappedto);
			i++;
		}
		
		return true;
	}
}
















