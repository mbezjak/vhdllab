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

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWireCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SimpleSchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SimpleSchemaWireCollection;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CanvasTest extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8933593424534057949L;


	public CanvasTest() {
		ISchemaComponentCollection comp = new SimpleSchemaComponentCollection();
		ISchemaWireCollection wir = new SimpleSchemaWireCollection();
	/*	
		try {
			comp.addComponent(10, 10, new DummyOR("OR1"));
		} catch (DuplicateKeyException e) {
			System.out.println("Nemogu napraviti komponentu");
			e.printStackTrace();
		} catch (OverlapException e) {
			System.out.println("Nemogu napraviti komponentu");
			e.printStackTrace();
		}
		*/
		SchemaCanvas can = new SchemaCanvas(comp, wir);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(can);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CanvasTest ct = new CanvasTest();
		ct.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		ct.pack();
		ct.setVisible(true);
	}

}
