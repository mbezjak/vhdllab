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
package hr.fer.zemris.vhdllab.applets.editor.schema2.model.parameters.generic;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;





public class Orientation implements IGenericValue {
	
	private static boolean empty = true;
	private static Set<Object> priv_allowed = new HashSet<Object>();
	public final static Set<Object> allAllowed = Collections.unmodifiableSet(priv_allowed);
	
	
	public EOrientation orientation = EOrientation.NORTH;
	
	public Orientation() { init(); }
	public Orientation(EOrientation ori) { init(); orientation = ori; }
	
	private void init() {
		if (empty) {
			empty = false;
			priv_allowed.add(new Orientation(EOrientation.NORTH));
			priv_allowed.add(new Orientation(EOrientation.SOUTH));
			priv_allowed.add(new Orientation(EOrientation.WEST));
			priv_allowed.add(new Orientation(EOrientation.EAST));
		}
	}
	
	public void deserialize(String code) {
		orientation = EOrientation.parse(code);
	}
	public String serialize() {
		return orientation.serialize();
	}
	public IGenericValue copyCtor() {
		return new Orientation(this.orientation);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Orientation)) return false;
		Orientation orient = (Orientation)obj;
		return (this.orientation == orient.orientation);
	}
	@Override
	public int hashCode() {
		return orientation.hashCode();
	}
	@Override
	public String toString() {
		return orientation.toString();
	}
	
}







