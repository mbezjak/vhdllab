package hr.fer.zemris.vhdllab.applets.schema2.model;

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
	
	
	
	private class ComponentIterator implements Iterator<PlacedComponent> {
		private Iterator<Entry<Caseless, PlacedComponent>> pit = components.entrySet().iterator();
		public boolean hasNext() {
			return pit.hasNext();
		}
		public PlacedComponent next() {
			return pit.next().getValue();
		}
		public void remove() {
			pit.remove();
		}
	}
	
	private Map<Caseless, PlacedComponent> components;
	
	
	
	public SimpleSchemaComponentCollection() {
		components = new HashMap<Caseless, PlacedComponent>();
	}
	
	
	
	
	
	
	public void addComponent(int x, int y, ISchemaComponent component) 
		throws DuplicateKeyException, OverlapException
	{
		if (components.containsKey(component.getName())) throw new DuplicateKeyException();
		
		// TODO: eventualna provjera overlappinga dode ovdje
		
		PlacedComponent wrapper = new PlacedComponent();
		wrapper.comp = component;
		wrapper.pos = new XYLocation(x, y);
		components.put(component.getName(), wrapper);
	}

	public boolean containsAt(int x, int y) {
		XYLocation loc = new XYLocation(x, y);
		XYLocation start;
		ISchemaComponent comp;
		for (Entry<Caseless, PlacedComponent> entry : components.entrySet()) {
			comp = entry.getValue().comp;
			start = entry.getValue().pos;
			if (loc.in(start.x, start.y, comp.getWidth(), comp.getHeight())) return true;
		}
		return false;
	}

	public boolean containsName(Caseless componentName) {
		return components.containsKey(componentName);
	}

	public ISchemaComponent fetchComponent(int x, int y) {
		XYLocation loc = new XYLocation(x, y);
		XYLocation start;
		ISchemaComponent comp;
		for (Entry<Caseless, PlacedComponent> entry : components.entrySet()) {
			comp = entry.getValue().comp;
			start = entry.getValue().pos;
			if (loc.in(start.x, start.y, comp.getWidth(), comp.getHeight())) return comp;
		}
		return null;
	}

	public ISchemaComponent fetchComponent(Caseless componentName) {
		return components.get(componentName).comp;
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
		return new ComponentIterator();
	}


	public void clear() {
		components.clear();
	}

	

	
}





