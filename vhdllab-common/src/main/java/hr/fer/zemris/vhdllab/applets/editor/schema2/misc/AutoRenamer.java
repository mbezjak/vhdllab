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

import java.util.Set;




/**
 * Trazi prvo slobodno ime na
 * temelju ponudenog i na temelju
 * popisa zauzetih imena.
 * 
 * @author brijest
 *
 */
public class AutoRenamer {

	
	public static Caseless getFreeName(Caseless offered, Set<Caseless> taken) {
		int i = 1;
		
		Caseless potname = offered;
		while (taken.contains(potname)) {
			potname = new Caseless(offered.toString() + ((i < 100) ? ("0" + i) : (i)));
			i++;
		}
		
		return potname;
	}
	
	
	/**
	 * Ako u procesu generiranja VHDLa neka komponenta
	 * zeli pomocni signal, treba zvati ovu metodu.
	 * 
	 * @param componentName
	 * @param wirenames
	 * @param index
	 */
	public static Caseless generateHelpSignalName(Caseless componentName, Set<Caseless> wirenames, int index) {
		componentName = new Caseless("sig_" + componentName.toString() + "_" + index);
		return getFreeName(componentName, wirenames);
	}
	
	
}




