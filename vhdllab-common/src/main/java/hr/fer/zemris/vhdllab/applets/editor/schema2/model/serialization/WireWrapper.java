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

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ParameterWrapper;

import java.util.ArrayList;
import java.util.List;



// TODO
public class WireWrapper {
	/* static fields */

	
	/* private fields */
	public List<WireSegment> segs;
	public List<ParameterWrapper> params;
	public String drawername;

	
	
	/* ctors */

	public WireWrapper() {
		segs = new ArrayList<WireSegment>();
		params = new ArrayList<ParameterWrapper>();
	}
	
	
	
	/* methods */

	public void addWireSegment(WireSegment ws) {
		segs.add(ws);
	}
	
	public void setDrawer(String name) {
		drawername = name;
	}
	
	public void addParameterWrapper(ParameterWrapper pw) {
		params.add(pw);
	}
	
}



















