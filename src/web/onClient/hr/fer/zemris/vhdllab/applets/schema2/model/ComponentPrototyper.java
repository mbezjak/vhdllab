package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.HashMap;
import java.util.Map;






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

	public ISchemaComponent clonePrototype(Caseless componentTypeName, Caseless instanceName)
	throws UnknownComponentPrototypeException {
		if (!prototypes.containsKey(componentTypeName))
			throw new UnknownComponentPrototypeException();
		
		ISchemaComponent comp = prototypes.get(componentTypeName).copyCtor();
		comp.setName(instanceName);
		
		return comp;
	}

	public Map<Caseless, ISchemaComponent> getPrototypes() {
		return prototypes;
	}

}








