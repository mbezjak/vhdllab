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
package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Decrementer implements ActionListener {
	
	private int maxIznos;
	private int iznos;
	private SchemaCanvas can;
	
	public Decrementer(int maxIznos, SchemaCanvas can) {
		this.maxIznos = maxIznos;
		iznos = maxIznos;
		this.can=can;
	}
	
	public int getIznos() {
		return iznos;
	}
	
	public void reset() {
		iznos = maxIznos;
	}
	


	public void actionPerformed(ActionEvent e) {
		iznos--;
		if(iznos == maxIznos/2) iznos = maxIznos;
		can.repaint();
	}
}
