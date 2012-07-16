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
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;




/**
 * Jednostavna kolekcija komponenti
 * u smislu da ima O(n) slozenost
 * dohvata po lokaciji.
 * Obavlja svoj posel bez obzira na to.
 * 
 * @author brijest
 *
 */
public class SimpleSchemaComponentCollection implements ISchemaComponentCollection {
	
	/* private fields */
	private Map<Caseless, PlacedComponent> components;
	private List<PlacedComponent> insertlist;
	private XYLocation loc;
	
	
	
	/* ctors */
	
	public SimpleSchemaComponentCollection() {
		components = new HashMap<Caseless, PlacedComponent>();
		insertlist = new LinkedList<PlacedComponent>();
		loc = new XYLocation();
	}
	
	
	
	
	
	/* methods */
	
	public void addComponent(int x, int y, ISchemaComponent component) 
		throws DuplicateKeyException, OverlapException
	{
		if (components.containsKey(component.getName())) throw new DuplicateKeyException();
		
		// TODO: eventualna provjera overlappinga dode ovdje
		
		PlacedComponent wrapper = new PlacedComponent();
		wrapper.comp = component;
		wrapper.pos = new XYLocation(x, y);
		components.put(component.getName(), wrapper);
		insertlist.add(wrapper);
	}

	public void addComponentAt(int x, int y, ISchemaComponent component, int index)
	throws DuplicateKeyException, OverlapException
	{
		if (components.containsKey(component.getName())) throw new DuplicateKeyException();
		if (index < 0 || index > insertlist.size()) throw new IndexOutOfBoundsException();
		
		// TODO: eventualna provjera overlappinga dode ovdje
		
		PlacedComponent wrapper = new PlacedComponent();
		wrapper.comp = component;
		wrapper.pos = new XYLocation(x, y);
		components.put(component.getName(), wrapper);
		insertlist.add(index, wrapper);		
	}

	public int getComponentIndex(Caseless name) throws UnknownKeyException {
		if (!components.containsKey(name)) throw new UnknownKeyException();		
		
		int count = 0;
		for (PlacedComponent plc : insertlist) {
			if (plc.comp.getName().equals(name)) return count;
			count++;
		}
		
		throw new IllegalStateException("Component exists but not found during iteration.");
	}

	public void reinsertComponent(Caseless name, int x, int y)
		throws UnknownKeyException, OverlapException
	{
		if (!components.containsKey(name)) throw new UnknownKeyException();
		
		// TODO eventualna provjera overlappinga
		
		PlacedComponent plc = components.get(name);
		components.remove(name);
		plc.pos.x = x;
		plc.pos.y = y;
		components.put(name, plc);
	}

	public void renameComponent(Caseless name, Caseless updatedname)
	throws UnknownKeyException, DuplicateKeyException
	{
		if (!components.containsKey(name)) throw new UnknownKeyException();
		if (components.containsKey(updatedname)) throw new DuplicateKeyException();
		
		PlacedComponent plc = components.get(name);
		components.remove(name);
		plc.comp.setName(updatedname);
		components.put(updatedname, plc);
	}
	
	public int size() {
		return components.size();
	}

	public boolean containsAt(int x, int y, int dist) {
		PlacedComponent plc;
		
		loc.x = x;
		loc.y = y;
		
		for (Entry<Caseless, PlacedComponent> entry : components.entrySet()) {
			plc = entry.getValue();
			if (loc.in(plc.pos.x - dist, plc.pos.y - dist,
					plc.comp.getWidth() + 2 * dist, plc.comp.getHeight() + 2 * dist)) return true;
		}
		return false;
	}

	public boolean containsName(Caseless componentName) {
		return components.containsKey(componentName);
	}

	public ISchemaComponent fetchComponent(int x, int y, int dist) {
		ISchemaComponent found = null;
		int mindist = dist + 1;
		
		loc.x = x;
		loc.y = y;
		
		for (Entry<Caseless, PlacedComponent> entry : components.entrySet()) {
			PlacedComponent plc = entry.getValue();
			int wdt = plc.comp.getWidth(), hgt = plc.comp.getHeight();
			
			if (loc.in(plc.pos.x, plc.pos.y, wdt, hgt)) return plc.comp;
			
			int plcdist = plc.pos.x - loc.x;
			if (loc.y >= (plc.pos.y - dist) && loc.y <= (plc.pos.y + hgt + dist)) {
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
				plcdist = loc.x - (plc.pos.x + wdt);
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
			}
			if (loc.x >= (plc.pos.x - dist) && loc.x <= (plc.pos.x + wdt + dist)) {
				plcdist = plc.pos.y - loc.y;
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
				plcdist = loc.y - (plc.pos.y + hgt);
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
			}
		}
		return found;
	}

	public ISchemaComponent fetchComponent(Caseless componentName) {
		PlacedComponent placedcomp = components.get(componentName);
		
		if (placedcomp == null) return null;
		return placedcomp.comp;
	}

	public Set<ISchemaComponent> fetchComponents(EComponentType componentType) {
		Set<ISchemaComponent> comps = new LinkedHashSet<ISchemaComponent>();
		
		for (PlacedComponent plc : insertlist) {
			if (plc.comp.getComponentType().equals(componentType)) comps.add(plc.comp);
		}
		
		return comps;
	}
	

	public XYLocation getComponentLocation(Caseless componentName) {
		PlacedComponent cpw = components.get(componentName);
		
		if (cpw == null) return null;

		return new XYLocation(cpw.pos);
	}


	public Rectangle getComponentBounds(Caseless componentName) {
		PlacedComponent cpw = components.get(componentName);
		
		if (cpw == null) return null;

		Rectangle bounds = new Rectangle();
		
		bounds.x = cpw.pos.x;
		bounds.y = cpw.pos.y;
		bounds.width = cpw.comp.getWidth();
		bounds.height = cpw.comp.getHeight();
		
		return bounds;
	}
	

	public void removeComponent(Caseless name) throws UnknownKeyException {
		if (!components.containsKey(name)) throw new UnknownKeyException();
		components.remove(name);
		Iterator<PlacedComponent> itp = insertlist.iterator();
		while (itp.hasNext()) {
			if (itp.next().comp.getName().equals(name)) {
				itp.remove();
				break;
			}
		}
	}
	
	
	public int distanceTo(Caseless name, int xfrom, int yfrom) {
		PlacedComponent plc = components.get(name);
		
		// no such component
		if (plc == null) return ISchemaComponentCollection.NO_COMPONENT;
		
		// is click within component
		int w = plc.comp.getWidth(), h = plc.comp.getHeight();
		if (xfrom >= plc.pos.x && yfrom >= plc.pos.y &&
				xfrom <= (plc.pos.x + w) && yfrom <= (plc.pos.y + h))
		{
			return 0;
		}
		
		// click is outside component
		int xmin, ymin;
		if (xfrom < plc.pos.x) {
			xmin = plc.pos.x - xfrom;
		} else {
			xmin = xfrom - (plc.pos.x + w);
		}
		if (yfrom < plc.pos.y) {
			ymin = plc.pos.y - yfrom;
		} else {
			ymin = yfrom - (plc.pos.y + h);
		}
		
		return (xmin < ymin) ? (xmin) : (ymin);
	}


	public Set<Caseless> getComponentNames() {
		return components.keySet();
	}
	

	public Iterator<PlacedComponent> iterator() {
		return insertlist.iterator();
	}


	public void clear() {
		components.clear();
	}

	

	
}





