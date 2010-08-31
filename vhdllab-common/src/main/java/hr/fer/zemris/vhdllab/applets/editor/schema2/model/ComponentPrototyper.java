/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	
	public boolean containsPrototype(Caseless prototypename) {
		return prototypes.containsKey(prototypename);
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

	public boolean removePrototype(Caseless prototypename) {
		if (!prototypes.containsKey(prototypename)) return false;
		
		prototypes.remove(prototypename);
		
		return true;
	}
	
	
	

}








