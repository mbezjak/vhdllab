package hr.fer.zemris.vhdllab.applets.schema2.model.serialization;

import java.util.ArrayList;
import java.util.List;





public class EntityWrapper {
	private List<ParameterWrapper> parameters;
	private List<PortWrapper> portwrappers;
	
	public EntityWrapper() {
		parameters = new ArrayList<ParameterWrapper>();
		portwrappers = new ArrayList<PortWrapper>();
	}

	
	
	public void setParameters(List<ParameterWrapper> parameters) {
		this.parameters = parameters;
	}

	public List<ParameterWrapper> getParameters() {
		return parameters;
	}
	
	public void addParameterWrapper(ParameterWrapper pw) {
		parameters.add(pw);
	}
	
	public void setPortwrappers(List<PortWrapper> portwrappers) {
		this.portwrappers = portwrappers;
	}

	public List<PortWrapper> getPortwrappers() {
		return portwrappers;
	}
	
	public void addPortWrapper(PortWrapper pw) {
		portwrappers.add(pw);
	}
	
	
}


















