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



/**
 * Enkapsulira postavke iscrtavanja komponenti i zica.
 * Predaje se pri pozivu <code>draw()</code> sucelja <code>IComponentDrawer</code>
 * i <code>IWireDrawer.</code>
 * Nije nuzno da se odredena implementacija sucelja <code>IComponentDrawer</code>
 * ili <code>IWireDrawer</code> drzi svih postavki iscrtavanja u slucaju
 * da se postavka da naslutiti iz samog crteza. Na primjer, ako se nazivi portova
 * daju naslutiti iz samog crteza komponente, onda odredena implementacija drawer-a
 * moze zaobici postavku ispisivanja imena portova.
 * 
 * Odredeni podatkovni clanovi ostavljeni su kao javni kako bi se omogucio
 * brzi pristup postavkama i samim time brze iscrtavanje. Duznost je programera
 * da pri implementaciji drawer-a NE MIJENJA te vrijednosti.
 * 
 * @author Axel
 *
 */
public class DrawingProperties {
	
	/* static fields */
	
	
	/* private fields */
	public boolean drawingPortNames;
	public boolean drawingComponentNames;
	public boolean drawingWireNames;
	
	
	/* ctors */

	public DrawingProperties() {
		drawingPortNames = true;
		drawingComponentNames = true;
		drawingWireNames = true;
	}
	
	
	/* methods */

	
	/**
	 * Postavlja ispis imena portova u slucaju da se radi o
	 * komponenti.
	 * @param drawingPortNames
	 */
	public void setDrawingPortNames(boolean drawingPortNames) {
		this.drawingPortNames = drawingPortNames;
	}
	
	/**
	 * Da li se ispisuju imena portova ako se radi o komponenti.
	 */
	public boolean isDrawingPortNames() {
		return drawingPortNames;
	}

	/**
	 * Postavlja iscrtavanje imena komponente.
	 * @param drawingNames
	 */
	public void setDrawingComponentNames(boolean drawingNames) {
		this.drawingComponentNames = drawingNames;
	}

	/**
	 * Da li se iscrtava ime komponente.
	 */
	public boolean isDrawingComponentNames() {
		return drawingComponentNames;
	}

	/**
	 * Postavlja iscrtavanje imena zice.
	 * @param drawingWireNames
	 */
	public void setDrawingWireNames(boolean drawingWireNames) {
		this.drawingWireNames = drawingWireNames;
	}
	
	/**
	 * Da li se iscrtava ime zice.
	 */
	public boolean isDrawingWireNames() {
		return drawingWireNames;
	}
	
}














