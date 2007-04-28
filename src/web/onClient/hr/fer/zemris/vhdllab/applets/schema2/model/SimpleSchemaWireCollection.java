package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;






public class SimpleSchemaWireCollection implements ISchemaWireCollection {
	private Map<Caseless, ISchemaWire> wires;
	private int xlkp, ylkp;
	
	
	
	
	// public metode
	
	
	public SimpleSchemaWireCollection() {
		wires = new HashMap<Caseless, ISchemaWire>();
	}
	
	
	
	public void addWire(ISchemaWire wire) throws DuplicateKeyException {
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

	public ISchemaWire fetchWire(Caseless wireName) {
		return wires.get(wireName); 
	}

	public void deserialize(String code) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public String serialize() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	
	
	
}









