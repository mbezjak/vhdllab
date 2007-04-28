package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.DefaultCircuitInterface;



public class SchemaEntity implements ISchemaEntity {
	private CircuitInterface circint;
	private IParameterCollection parameters;
	
	
	public SchemaEntity() {
		circint = new DefaultCircuitInterface("");
		parameters = new SchemaParameterCollection();
	}
	
	

	public CircuitInterface getCircuitInterface() {
		return circint;
	}

	public IParameterCollection getParameters() {
		return parameters;
	}

	public void setCircuitInterface(CircuitInterface circuitInterface) {
		circint = circuitInterface;
	}

	public void setParameters(IParameterCollection parameterCollection) {
		parameters = parameterCollection;
	}

	
}
