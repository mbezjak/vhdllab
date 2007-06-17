package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.CaselessParameter;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.ArrayList;
import java.util.List;




public class SchemaEntity implements ISchemaEntity {
	
	/* static fields */
	public static final String KEY_NAME = "Name";
	
	
	/* private fields */
	private IParameterCollection parameters;
	private List<Port> ports;
	
	
	
	/**
	 * Koristi se kod deserijalizacije. Ovaj konstruktor NECE
	 * dodati defaultne parametre (npr. Name).
	 * 
	 * @param wrapper
	 */
	public SchemaEntity() {
		parameters = new SchemaParameterCollection();
		ports = new ArrayList<Port>();
	}
	
	
	/**
	 * Stvara entity. Dodaje defaultne parametre.
	 * 
	 * @param circIntName
	 * Ocekuje se Caseless ime sucelja sklopa.
	 */
	public SchemaEntity(Caseless circIntName) {
		parameters = new SchemaParameterCollection();
		ports = new ArrayList<Port>();
		initDefaultParameters();
	}
	
	private void initDefaultParameters() {
		IParameter param = new CaselessParameter(KEY_NAME, false, new Caseless("schema01"));
		parameters.addParameter(param);
	}
	
	
	

	public CircuitInterface getCircuitInterface() {
		throw new NotImplementedException();
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public void setParameters(IParameterCollection parameterCollection) {
		parameters = parameterCollection;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}
	
	public List<Port> getPorts() {
		return ports;
	}

	public void reset() {
		ports.clear();
		parameters.clear();
		initDefaultParameters();
	}
	
	
}














