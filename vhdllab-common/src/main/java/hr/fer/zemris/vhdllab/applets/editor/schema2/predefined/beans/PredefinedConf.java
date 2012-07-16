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

public class PredefinedConf {

	private List<PredefinedComponent> components;
	
	public PredefinedConf() {
		components = new ArrayList<PredefinedComponent>();
	}

	public List<PredefinedComponent> getComponents() {
		return components;
	}

	public void setComponents(List<PredefinedComponent> components) {
		this.components = components;
	}
	
	public void addPredefinedComponent(PredefinedComponent c) {
		components.add(c);
	}
	
	@Override
	public String toString() {
		return "Predefined Configuration {\n" + 
			components.toString() + "\n}";
	}
	
}
