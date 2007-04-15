package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaWireCollection;






public class SimpleSchemaWireCollection implements ISchemaWireCollection {

	
	
	
	public void addWire(ISchemaWire wire) throws DuplicateKeyException {
		// TODO Auto-generated method stub
		
	}

	public void removeWire(String wireName) throws UnknownKeyException {
		// TODO Auto-generated method stub
		
	}

	public boolean containsAt(int x, int y, int dist) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsName(String wireName) {
		// TODO Auto-generated method stub
		return false;
	}

	public ISchemaWire fetchWire(int x, int y, int dist) {
		// TODO Auto-generated method stub
		return null;
	}

	public ISchemaWire fetchWire(String wireName) {
		// TODO Auto-generated method stub
		return null;
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
