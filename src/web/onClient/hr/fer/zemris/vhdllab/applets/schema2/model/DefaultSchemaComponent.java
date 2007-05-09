package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SMath;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.List;



public class DefaultSchemaComponent implements ISchemaComponent {
	private final int WIDTH_PER_PORT = 30;
	private final int HEIGHT_PER_PORT = 30;
	
	private PredefinedComponent predefcomp;
	private IComponentDrawer compdrawer;
	private EOrientation orientation;
	private CircuitInterface circint;
	private IParameterCollection parameters;
	private List<SchemaPort> ports;
	
	
	
	/**
	 * Stvara objekt tipa ISchemaComponent na
	 * temelju predane predefinirane komponente.
	 * 
	 * @param predefComponent
	 * Wrapper klasa (dobivena iz npr. xml-a) na temelju
	 * koje se konstruira ova klasa.
	 */
	public DefaultSchemaComponent(PredefinedComponent predefComponent) {
		predefcomp = predefComponent;
		parameters = new SchemaParameterCollection();
		
		// izraditi CircuitInterface
		
		// stvoriti parametar name i povezati ga
		
		// postaviti orijentaciju i povezati je
		// s istoimenim parametrom
		orientation = EOrientation.NORTH;
		
		// izgraditi listu portova i povezati je s
		// parametrima
		
		// pozvati reflection i stvoriti compdrawer
	}


	public ISchemaComponent copyCtor() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategoryName() {
		return predefcomp.getCategoryName();
	}

	public CircuitInterface getCircuitInterface() {
		return circint;
	}

	public EOrientation getComponentOrientation() {
		return orientation;
	}

	public IComponentDrawer getDrawer() {
		return compdrawer;
	}

	public int getHeight() {
		// TODO zbrojiti po portovima i rotirati
		return 0;
	}

	public Caseless getName() {
		return new Caseless(circint.getEntityName());
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public List<SchemaPort> getPorts() {
		return ports;
	}

	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		int index = SMath.calcClosestPort(xoffset, yoffset, dist, ports);
		
		if (index == -1) return null;
		else return ports.get(index);
	}

	public SchemaPort getSchemaPort(int index) {
		return ports.get(index);
	}

	public Caseless getTypeName() {
		return new Caseless(predefcomp.getComponentName());
	}

	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		// TODO ovo napraviti
		return null;
	}

	public int getWidth() {
		// TODO zbrojiti po portovima i rotirati
		return 0;
	}

	public int portCount() {
		return ports.size();
	}

	public void setComponentOrientation(EOrientation orient) {
		// TODO Auto-generated method stub
	}

	public void setName(Caseless name) {
		// TODO Auto-generated method stub
	}

	public void deserialize(String code) {
		throw new NotImplementedException();
	}

	public String serialize() {
		throw new NotImplementedException();
	}

}
