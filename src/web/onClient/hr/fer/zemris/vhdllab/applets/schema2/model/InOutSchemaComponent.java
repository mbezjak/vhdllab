package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ComponentWrapper;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;









/**
 * Klasa modelira komponentu koja predstavlja
 * ulaz ili izlaz na modeliranom sklopu.
 * 
 * @author brijest
 *
 */
public class InOutSchemaComponent implements ISchemaComponent {
	
	/* static fields */

	
	
	/* private fields */

	
	
	
	/* ctors */

	public InOutSchemaComponent(ComponentWrapper wrapper) {
	}
	
	
	
	
	
	
	
	
	
	/* methods */

	public ISchemaComponent copyCtor() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deserialize(ComponentWrapper compwrap) {
		// TODO Auto-generated method stub
		
	}

	public String getCategoryName() {
		return null;
	}

	public CircuitInterface getCircuitInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCodeFileName() {
		return null;
	}

	public EOrientation getComponentOrientation() {
		throw new NotImplementedException();
	}

	public IComponentDrawer getDrawer() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Caseless getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IParameterCollection getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public SchemaPort getSchemaPort(Caseless name) {
		// TODO Auto-generated method stub
		return null;
	}

	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		// TODO Auto-generated method stub
		return null;
	}

	public SchemaPort getSchemaPort(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SchemaPort> getSchemaPorts() {
		// TODO Auto-generated method stub
		return null;
	}

	public Caseless getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isGeneric() {
		// TODO Auto-generated method stub
		return false;
	}

	public int portCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<Port> portIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator<SchemaPort> schemaPortIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setComponentOrientation(EOrientation orient) {
		throw new NotImplementedException();
	}

	public void setName(Caseless name) {
		// TODO Auto-generated method stub
		
	}
	
}












