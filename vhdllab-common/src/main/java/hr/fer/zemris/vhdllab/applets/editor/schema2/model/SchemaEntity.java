package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.ParameterNotFoundException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.TextParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;




public class SchemaEntity implements ISchemaEntity {
	
	/* static fields */
	private static final String KEY_DESCRIPTION = "Description";	
	
	/* private fields */
	private IParameterCollection parameters;
	
	
	
	/**
	 * Koristi se kod deserijalizacije. Ovaj konstruktor NECE
	 * dodati defaultne parametre (npr. Name).
	 * 
	 * @param wrapper
	 */
	public SchemaEntity() {
		parameters = new SchemaParameterCollection();
		initDefaultParameters(new Caseless("schema"));
	}
	
	
	/**
	 * Stvara entity. Dodaje defaultne parametre.
	 * 
	 * @param circIntName
	 * Ocekuje se Caseless ime sucelja sklopa.
	 */
	public SchemaEntity(Caseless circIntName) {
		parameters = new SchemaParameterCollection();
		initDefaultParameters(circIntName);
	}
	
	private void initDefaultParameters(Caseless initialName) {
		parameters.clear();
		IParameter nameparam = new CaselessParameter(KEY_NAME, false, initialName);
		parameters.addParameter(nameparam);
		IParameter txtparam = new TextParameter(KEY_DESCRIPTION, false, "");
		parameters.addParameter(txtparam);
	}
	
	
	

	public CircuitInterface getCircuitInterface(ISchemaInfo info) {
		try {
			return new CircuitInterface(
					((Caseless)(parameters.getParameter(KEY_NAME).getValue())).toString(),
					getPorts(info));
		} catch (ParameterNotFoundException e) {
			throw new IllegalStateException("No name parameter within entity.", e);
		}
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public Caseless getName() {
		try {
			return (Caseless) parameters.getParameter(KEY_NAME).getValue();
		} catch (ParameterNotFoundException e) {
			throw new IllegalStateException("Must contain parameter '" + KEY_NAME + "'.");
		}
	}


	public void setParameters(IParameterCollection parameterCollection) {
		parameters = parameterCollection;
	}
	
	public List<Port> getPorts(ISchemaInfo info) {
		ISchemaComponentCollection components = info.getComponents();
		Set<ISchemaComponent> inouts = components.fetchComponents(EComponentType.IN_OUT);
		List<Port> ports = new ArrayList<Port>();
		
		for (ISchemaComponent cmp : inouts) {
			Port tocopy = cmp.getPort(0);
			ports.add(new Port(cmp.getName().toString(), tocopy.getDirection(), tocopy.getType()));
//			ports.add(new DefaultPort(
//					tocopy.getName(),
//					(tocopy.getDirection().equals(Direction.IN)) ? (Direction.OUT) : (Direction.IN),
//					tocopy.getType()
//					));
		}
		
		return ports;
	}

	public void reset() {
		Caseless name = getName();
		initDefaultParameters(name);
	}
	
	
}














