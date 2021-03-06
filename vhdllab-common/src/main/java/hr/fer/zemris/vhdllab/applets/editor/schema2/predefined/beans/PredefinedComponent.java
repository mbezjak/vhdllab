/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;


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
