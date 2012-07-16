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
package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.ArrayList;
import java.util.List;

public class PortRelation {
	public Port port;
	public EOrientation orientation;
	public List<SchemaPort> relatedTo;

	public PortRelation(Port p, EOrientation orient) {
		port = p;
		orientation = orient;
		relatedTo = new ArrayList<SchemaPort>();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof PortRelation)) return false;
		PortRelation pr = (PortRelation)obj;
		return (pr.port.equals(this.port) && pr.relatedTo.equals(this.relatedTo));
	}

	@Override
	public int hashCode() {
		return port.hashCode() << 16 + relatedTo.hashCode();
	}
	
	
}
