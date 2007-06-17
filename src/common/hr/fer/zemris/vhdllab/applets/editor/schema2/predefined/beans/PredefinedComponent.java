package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;

import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.PortWrapper;

import java.util.ArrayList;
import java.util.List;

public class PredefinedComponent {

	private String componentName;
	private String codeFileName;
	private String drawerName;
	private String categoryName;
	private String preferredName;
	private Boolean genericComponent;
	private List<ParameterWrapper> parameters;
	private List<PortWrapper> ports;

	public PredefinedComponent() {
		parameters = new ArrayList<ParameterWrapper>();
		ports = new ArrayList<PortWrapper>();
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String parameterName) {
		if (parameterName.trim().equals("")) {
			parameterName = null;
		}
		this.componentName = parameterName;
	}

	public String getCodeFileName() {
		return codeFileName;
	}

	public void setCodeFileName(String codeFileName) {
		if (codeFileName.trim().equals("")) {
			codeFileName = null;
		}
		this.codeFileName = codeFileName;
	}

	public String getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(String drawerName) {
		if (drawerName.trim().equals("")) {
			drawerName = null;
		}
		this.drawerName = drawerName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		if (categoryName.trim().equals("")) {
			categoryName = null;
		}
		this.categoryName = categoryName;
	}

	public Boolean isGenericComponent() {
		return genericComponent;
	}

	public void setGenericComponent(Boolean genericComponent) {
		this.genericComponent = genericComponent;
	}

	public void addParameter(ParameterWrapper p) {
		parameters.add(p);
	}

	public List<ParameterWrapper> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterWrapper> parameters) {
		this.parameters = parameters;
	}

	public void addPort(PortWrapper p) {
		ports.add(p);
	}

	public List<PortWrapper> getPorts() {
		return ports;
	}

	public void setPorts(List<PortWrapper> ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return "Predefined Component {\n" + componentName + "\n" + codeFileName
				+ "\n" + drawerName + "\n" + categoryName + "\n"
				+ genericComponent + "\n" + parameters + "\n" + ports + "\n}";
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getPreferredName() {
		return preferredName;
	}

}
