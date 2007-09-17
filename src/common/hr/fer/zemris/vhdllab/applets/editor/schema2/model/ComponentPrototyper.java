package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.AutoRenamer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;






public class ComponentPrototyper implements ISchemaPrototypeCollection {
	private Map<Caseless, ISchemaComponent> prototypes;
	
	
	public ComponentPrototyper() {
		prototypes = new HashMap<Caseless, ISchemaComponent>();
	}

	public void addPrototype(ISchemaComponent componentPrototype) throws DuplicateKeyException {
		if (prototypes.containsKey(componentPrototype.getTypeName()))
			throw new DuplicateKeyException();
		prototypes.put(componentPrototype.getTypeName(), componentPrototype);
	}

	public ISchemaComponent clonePrototype(Caseless componentTypeName, Set<Caseless> takennames)
		throws UnknownComponentPrototypeException
	{
		if (!prototypes.containsKey(componentTypeName))
			throw new UnknownComponentPrototypeException("Cannot find prototype '" + componentTypeName + "'.");
		
		ISchemaComponent comp = prototypes.get(componentTypeName).copyCtor();
		comp.setName(AutoRenamer.getFreeName(comp.getName(), takennames));
		
		return comp;
	}

	public Map<Caseless, ISchemaComponent> getPrototypes() {
		return prototypes;
	}

	public void clearPrototypes() {
		prototypes.clear();
	}

	public void clearPrototypes(EComponentType cmptype) {
		Iterator<Entry<Caseless, ISchemaComponent>> iter = prototypes.entrySet().iterator();
		
		while (iter.hasNext()) {
			if (iter.next().getValue().getComponentType().equals(cmptype)) iter.remove();
		}
	}
	
	

}








