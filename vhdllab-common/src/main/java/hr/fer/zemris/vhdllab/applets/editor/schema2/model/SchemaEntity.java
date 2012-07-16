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
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.TextParameter;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

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
			CircuitInterface ci = new CircuitInterface(
					((Caseless)(parameters.getParameter(KEY_NAME).getValue())).toString());
			ci.addAll(getPorts(info));
            return ci;
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
			Port copiedPort = new Port(tocopy);
			copiedPort.setName(cmp.getName().toString());
			ports.add(copiedPort);
		}
		
		return ports;
	}

	public void reset() {
		Caseless name = getName();
		initDefaultParameters(name);
	}
	
	
}














