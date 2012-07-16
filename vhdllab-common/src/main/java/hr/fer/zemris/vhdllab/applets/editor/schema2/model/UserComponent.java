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

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PortRelation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.drawers.DefaultComponentDrawer;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PortWrapper;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.List;




/**
 * Predstavlja user defined komponentu na shemi.
 * 
 * @author Axel
 *
 */
public class UserComponent extends DefaultSchemaComponent {

	/* static fields */
	

	/* private fields */
	
	
	

	/* ctors */
	
	
	/**
	 * Samo za copyCtor().
	 */
	protected UserComponent() {
		super();
	}
	
	public UserComponent(CircuitInterface cint) {
		super(preparePredefinedWrapper(cint));
	}
	
	/**
	 * Deserijalizacija.
	 * 
	 * @param wrapper
	 */
	public UserComponent(ComponentWrapper wrapper) {
		super(wrapper);
	}
	
	
	
	
	

	/* methods */	

	
	@Override
	public EComponentType getComponentType() {
		return EComponentType.USER_DEFINED;
	}
	
	@Override
	public ISchemaComponent copyCtor() {
		UserComponent uc = new UserComponent();
		
		uc.componentName = this.componentName;
		uc.codeFileName = this.codeFileName;
		uc.categoryName = this.categoryName;
		uc.generic = this.generic;
		uc.width = this.width;
		uc.height = this.height;
		uc.initDrawer(this.drawer.getClass().getName());
		
		// copy schema ports and port relations
		PortRelation npr;
		SchemaPort nsp;
		for (PortRelation portrel : this.portrelations) {
			npr = new PortRelation(
					new Port(portrel.port),
					portrel.orientation
					);
			for (SchemaPort sp : portrel.relatedTo) {
				nsp = new SchemaPort(sp);
				nsp.setMapping(null);
				npr.relatedTo.add(nsp);
				uc.schemaports.add(nsp);
			}
			uc.portrelations.add(npr);
		}
		
		// copy parameters
		for (IParameter param : this.parameters) {
			uc.parameters.addParameter(param.copyCtor());
		}
		
		return uc;
	}

	private static PredefinedComponent preparePredefinedWrapper(CircuitInterface cint) {
		PredefinedComponent predef = new PredefinedComponent();
		String name = cint.getName();
		
		// init basic fields
		predef.setComponentName(name);
		predef.setCodeFileName(name);
		predef.setCategoryName(Constants.USER_CATEGORY_NAME);
		predef.setPreferredName(name + Constants.PREFERRED_NAME_SUFIX);
		predef.setDrawerName(DefaultComponentDrawer.class.getName());
		predef.setGenericComponent(false);
		
		// init parameters
		
		// init ports - put IN on WEST, and OUT ports on EAST
		List<Port> ports = cint.getPorts();
		for (Port p : ports) {
			PortWrapper wrapper = new PortWrapper(p, null);
			if (p.isIN()) {
				wrapper.setOrientation(PortWrapper.ORIENTATION_WEST);
			} else if (p.isOUT()) {
				wrapper.setOrientation(PortWrapper.ORIENTATION_EAST);
			} else {
				throw new IllegalArgumentException("Direction '" + p.getDirection() + "' not implemented.");
			}
			predef.addPort(wrapper);
		}
		
		return predef;
	}

}


















