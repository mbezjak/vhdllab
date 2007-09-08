package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;






public class SimpleSchemaWireCollection implements ISchemaWireCollection {
	
	private class WireIterator implements Iterator<ISchemaWire> {
		private Iterator<Entry<Caseless, ISchemaWire>> pit = wires.entrySet().iterator();
		public boolean hasNext() {
			return pit.hasNext();
		}
		public ISchemaWire next() {
			return pit.next().getValue();
		}
		public void remove() {
			pit.remove();
		}
	}
	
	
	/* private fields */
	private Map<Caseless, ISchemaWire> wires;
	
	
	
	/* ctors */
	
	public SimpleSchemaWireCollection() {
		wires = new LinkedHashMap<Caseless, ISchemaWire>();
	}
	
	
	/* methods */
	
	public void addWire(ISchemaWire wire) throws DuplicateKeyException, OverlapException {
		if (wires.containsKey(wire.getName())) throw new DuplicateKeyException();
		wires.put(wire.getName(), wire);
	}

	public void removeWire(Caseless wireName) throws UnknownKeyException {
		if (!wires.containsKey(wireName)) throw new UnknownKeyException();
		wires.remove(wireName);
	}

	public boolean containsAt(int x, int y, int dist) {
		for (Entry<Caseless, ISchemaWire> entry : wires.entrySet()) {
			List<WireSegment> segments = entry.getValue().getSegments();
			
			for (WireSegment seg : segments) {
				if (seg.calcDist(x, y) <= dist) return true;
			}
		}
		return false;
	}

	public boolean containsName(Caseless wireName) {
		return (wires.containsKey(wireName));
	}

	public ISchemaWire fetchWire(int x, int y, int dist) {
		for (Entry<Caseless, ISchemaWire> entry : wires.entrySet()) {
			List<WireSegment> segments = entry.getValue().getSegments();
			
			for (WireSegment seg : segments) {
				if (seg.calcDist(x, y) <= dist) return entry.getValue();
			}
		}
		return null;
	}

	public Set<ISchemaWire> fetchAllWires(int x, int y) {
		Set<ISchemaWire> allwires = null;
		Set<Entry<Caseless, ISchemaWire>> entries = wires.entrySet();
		Iterator<Entry<Caseless, ISchemaWire>> it = entries.iterator();
		
		while (it.hasNext()) {
			ISchemaWire sw = it.next().getValue();
			
			for (WireSegment seg : sw.getSegments()) {
				if (seg.hasPoint(x, y)) {
					allwires = new HashSet<ISchemaWire>();
					allwires.add(sw);
					break;
				}
			}
			
			if (allwires != null) break;
		}
		
		while (it.hasNext()) {
			ISchemaWire sw = it.next().getValue();
			
			for (WireSegment seg : sw.getSegments()) {
				if (seg.hasPoint(x, y)) {
					allwires.add(sw);
				}
			}
		}
		
		return allwires;
	}


	public ISchemaWire fetchWire(Caseless wireName) {
		return wires.get(wireName); 
	}
	
	

	public Rect2d getBounds(Caseless wireName) {
		ISchemaWire wire = wires.get(wireName);
		
		if (wire == null) return null;
		else return wire.getBounds();
	}



	public Set<Caseless> getWireNames() {
		return wires.keySet();
	}



	public Iterator<ISchemaWire> iterator() {
		return new WireIterator();
	}
	
	public void clear() {
		wires.clear();
	}

	
	
	
}









