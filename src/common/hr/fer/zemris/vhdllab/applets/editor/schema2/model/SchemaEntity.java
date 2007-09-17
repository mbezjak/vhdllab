package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

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
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultPort;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;




public class SchemaEntity implements ISchemaEntity {
	
	/* static fields */
	
	
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
	}
	
	
	/**
	 * Stvara entity. Dodaje defaultne parametre.
	 * 
	 * @param circIntName
	 * Ocekuje se Caseless ime sucelja sklopa.
	 */
	public SchemaEntity(Caseless circIntName) {
		parameters = new SchemaParameterCollection();
		initDefaultParameters();
	}
	
	private void initDefaultParameters() {
		IParameter param = new CaselessParameter(KEY_NAME, false, new Caseless("schema01"));
		parameters.addParameter(param);
	}
	
	
	

	public CircuitInterface getCircuitInterface(ISchemaInfo info) {
		try {
			return new DefaultCircuitInterface(
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
			ports.add(new DefaultPort(cmp.getName().toString(), tocopy.getDirection(), tocopy.getType()));
//			ports.add(new DefaultPort(
//					tocopy.getName(),
//					(tocopy.getDirection().equals(Direction.IN)) ? (Direction.OUT) : (Direction.IN),
//					tocopy.getType()
//					));
		}
		
		return ports;
	}

	public void reset() {
		parameters.clear();
		initDefaultParameters();
	}
	
	
}














