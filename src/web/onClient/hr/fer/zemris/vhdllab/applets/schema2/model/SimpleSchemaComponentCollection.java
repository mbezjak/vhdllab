package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

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
		int mindist = dist;
		
		loc.x = x;
		loc.y = y;
		
		for (Entry<Caseless, PlacedComponent> entry : components.entrySet()) {
			PlacedComponent plc = entry.getValue();
			int wdt = plc.comp.getWidth(), hgt = plc.comp.getHeight();
			
			if (loc.in(plc.pos.x, plc.pos.y, wdt, hgt)) return plc.comp;
			
			int plcdist = plc.pos.x - loc.x;
			if (loc.y > (plc.pos.y - dist) && loc.y < (plc.pos.y + hgt + dist)) {
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
				plcdist = loc.x - (plc.pos.x + wdt);
				if (plcdist < mindist && plcdist >= 0) { mindist = plcdist; found = plc.comp; }
			}
			if (loc.x > (plc.pos.x - dist) && loc.x < (plc.pos.x + wdt + dist)) {
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
		else return new XYLocation(cpw.pos);
	}


	public Rectangle getComponentBounds(Caseless componentName) {
		PlacedComponent cpw = components.get(componentName);
		
		if (cpw == null) return null;
		else {
			Rectangle bounds = new Rectangle();
			
			bounds.x = cpw.pos.x;
			bounds.y = cpw.pos.y;
			bounds.width = cpw.comp.getWidth();
			bounds.height = cpw.comp.getHeight();
			
			return bounds;
		}
	}
	

	public void removeComponent(Caseless name) throws UnknownKeyException {
		if (!components.containsKey(name)) throw new UnknownKeyException();
		components.remove(name);
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





