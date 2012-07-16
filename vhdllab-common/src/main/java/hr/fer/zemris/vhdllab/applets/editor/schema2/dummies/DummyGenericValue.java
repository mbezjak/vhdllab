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
package hr.fer.zemris.vhdllab.applets.editor.schema2.dummies;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IGenericValue;







public class DummyGenericValue implements IGenericValue {
	
	private Float floatek = 0.f;
	

    public IGenericValue copyCtor() {
    	DummyGenericValue dgv = new DummyGenericValue();
    	dgv.floatek = this.floatek;
    	return dgv;
    }

    public void deserialize(String code) {
		floatek = Float.parseFloat(code);
    }

    public String serialize() {
		return floatek.toString();
    }

	@Override
	public String toString() {
		return floatek.toString();
	}
    
    
}








