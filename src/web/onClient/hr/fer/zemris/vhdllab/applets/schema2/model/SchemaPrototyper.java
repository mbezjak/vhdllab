package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownComponentPrototypeException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;






public class SchemaPrototyper implements ISchemaPrototypeCollection {
	private Map<Caseless, ISchemaComponent> prototypes;
	
	
	public SchemaPrototyper() {
		prototypes = new HashMap<Caseless, ISchemaComponent>();
	}

	public void addPrototype(ISchemaComponent componentPrototype) throws DuplicateKeyException {
		if (prototypes.containsKey(componentPrototype.getTypeName()))
			throw new DuplicateKeyException();
		prototypes.put(componentPrototype.getTypeName(), componentPrototype);
	}

	public ISchemaComponent clonePrototype(Caseless componentTypeName) throws UnknownComponentPrototypeException {
		if (!prototypes.containsKey(componentTypeName))
			throw new UnknownComponentPrototypeException();
		return prototypes.get(componentTypeName).copyCtor();
	}

	public Set<Caseless> getPrototypeNames() {
		return prototypes.keySet();
	}
	
	public Collection<ISchemaComponent> getPrototypes() {
		return prototypes.values();
	}

}








