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
package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.DrawingProperties;

import java.awt.Graphics2D;



/**
 * Sucelje koje sluzi za iscrtavanje sklopa
 * po nekom platnu.
 * Objekt koji implementira ovo sucelje vraca
 * svaka komponenta.
 * 
 * Drawer u nacelu ne smije mijenjati stanje
 * graphics objekta, ili ga u najgorem slucaju
 * smije ostaviti u istom stanju u kakvom ga je
 * dobio (npr. smije promijeniti stanje, ali ga
 * onda mora vratiti u prvotno).
 * 
 * Svaka implementacija Drawer-a MORA imati jedan
 * konstruktor koji prima ISchemaComponent.
 * 
 * @author brijest
 *
 */
public interface IComponentDrawer {
	
	/**
	 * Iscrtava sklop. Same informacije
	 * o sklopu koji se iscrtava (velicina,
	 * broj i polozaj portova) sadrzi
	 * konkretna implementacija.
	 * 
	 * @param graphics
	 * Adapter za iscrtavanje.
	 */
	void draw(Graphics2D graphics, DrawingProperties properties);
}













