package hr.fer.zemris.vhdllab.applets.schema2.model;

import java.util.List;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;



public class DefaultSchemaComponent implements ISchemaComponent {
	
	
	
	
	/**
	 * Stvara objekt tipa ISchemaComponent na
	 * temelju predane predefinirane komponente.
	 * 
	 * @param predefComponent
	 * Wrapper klasa (dobivena iz npr. xml-a) na temelju
	 * koje se konstruira ova klasa.
	 */
	public DefaultSchemaComponent(PredefinedComponent predefComponent) {
		// TODO Auto-generated constructor stub
	}


	public ISchemaComponent copyCtor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	public CircuitInterface getCircuitInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	public EOrientation getComponentOrientation() {
		// TODO Auto-generated method stub
		return null;
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

	public List<SchemaPort> getPorts() {
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

	public int portCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setComponentOrientation(EOrientation orient) {
		// TODO Auto-generated method stub

	}

	public void setName(Caseless name) {
		// TODO Auto-generated method stub

	}

	public void deserialize(String code) {
		// TODO Auto-generated method stub

	}

	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
