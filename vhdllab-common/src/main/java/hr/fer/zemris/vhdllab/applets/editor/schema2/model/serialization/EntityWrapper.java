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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;

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


















