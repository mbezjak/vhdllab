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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.events;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterEvent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic.ParamPort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Analizira port i postavlja schema portove (pinove).
 * Moze pripadati iskljucivo parametru InOutComponent-a,
 * i to parametru koji je po tipu OBJECT i sadrzi vrijednost
 * ParamPort.
 * 
 * @author Axel
 *
 */
public class InOutPortChanger implements IParameterEvent {

	/* static fields */
	private static final List<ChangeTuple> changes = new ArrayList<ChangeTuple>();
	private static final List<ChangeTuple> ro_ch = Collections.unmodifiableList(changes);
	static {
		changes.add(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}

	/* private fields */
	
	

	/* ctors */
	
	public InOutPortChanger() {
	}
	

	/* methods */

	public IParameterEvent copyCtor() {
		return new InOutPortChanger();
	}
	
	public List<ChangeTuple> getChanges() {
		return ro_ch;
	}
	
	public boolean isUndoable() {
		return false;
	}
	
	public boolean performChange(Object oldvalue, IParameter parameter,
			ISchemaInfo info, ISchemaWire wire, ISchemaComponent component)
	{
		if (component == null || !(component instanceof InOutSchemaComponent))
				throw new IllegalArgumentException("This parameter only works for InOutComponents.");
		
		if (!(oldvalue instanceof ParamPort))
			throw new IllegalArgumentException("Old value should be instance of ParamPort.");
		
		Object newvalue = parameter.getValue();
		
		if (!(newvalue instanceof ParamPort))
			throw new IllegalStateException("Parameter contains a value which is not a ParamPort.");
		
		InOutSchemaComponent inoutcmp = (InOutSchemaComponent)component;
		ParamPort oldport = (ParamPort)oldvalue, newport = (ParamPort)newvalue;
		
		if (oldport.getPort().equals(newport.getPort())) return true;
		
		inoutcmp.setPort(newport.getPort());
		inoutcmp.rebuildSchemaPorts();
		
		return true;
	}
	
}














