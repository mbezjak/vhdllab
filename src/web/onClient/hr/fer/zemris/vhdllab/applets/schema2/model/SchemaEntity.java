package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;




public class SchemaEntity implements ISchemaEntity {
	
	
	/* private fields */
	private IParameterCollection parameters;
	
	
	/**
	 * Stvara entity.
	 * 
	 * @param circIntName
	 * Ocekuje se Caseless ime sucelja sklopa.
	 */
	public SchemaEntity(Caseless circIntName) {
		parameters = new SchemaParameterCollection();
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

	
}














