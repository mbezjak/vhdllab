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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.QueryResult;

import java.util.ArrayList;
import java.util.List;


/**
 * Vraca listu apsolutnih pozicija pinova na koje je spojena
 * specificirana zica.
 * 
 * Vraca objekt tipa List<XYLocation> pod
 * kljucem KEY_PIN_LOCATIONS.
 * 
 * @author Axel
 *
 */
public class FindConnectedPins implements IQuery {

	/* static fields */
	private static final List<EPropertyChange> DEPENDENT_ON = new ArrayList<EPropertyChange>();
	public static final String KEY_PIN_LOCATIONS = "pin_locations";
	
	static {
		DEPENDENT_ON.add(EPropertyChange.ANY_CHANGE);
	}
	
	/* private fields */
	private Caseless wirename;
	
	/* ctors */
	
	public FindConnectedPins(Caseless nameOfWire) {
		wirename = nameOfWire;
	}
	
	/* methods */
	
	@Override
	public List<EPropertyChange> getPropertyDependency() {
		return DEPENDENT_ON;
	}

	@Override
	public String getQueryName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean isCacheable() {
		return true;
	}

	@Override
	public IQueryResult performQuery(ISchemaInfo info) {
		// check if wire exists
		if (!info.getWires().containsName(wirename)) return new QueryResult(false);
		
		// get pins this wire is connected to
		List<XYLocation> positions = new ArrayList<XYLocation>();
		for (PlacedComponent placed : info.getComponents()) {
		    if (placed.comp != null) {
		        for (SchemaPort sp : placed.comp.getSchemaPorts()) {
		            Caseless mappedto = sp.getMapping();
		            if (Caseless.isNullOrEmpty(mappedto)) continue;
		            if (!mappedto.equals(wirename)) continue;
		            positions.add(new XYLocation(placed.pos.x + sp.getOffset().x,
		                    placed.pos.y + sp.getOffset().y));
		        }
		    }
		}
		
		return new QueryResult(KEY_PIN_LOCATIONS, positions);
	}

}














