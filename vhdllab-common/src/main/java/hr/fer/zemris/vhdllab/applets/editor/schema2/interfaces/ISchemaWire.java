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

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.WireSegment;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;

import java.util.List;
import java.util.Set;



/**
 * Sucelje koje opisuje bilo koju zicu (signal)
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaWire {
	
	/**
	 * Kljuc koji bi se trebao naci u svakoj kolekciji
	 * parametara.
	 */
	public static final String KEY_NAME = "Name";
	
	
	/**
	 * Virtualni copy konstruktor.
	 * Koristi se u ISchemaPrototypeCollection.
	 * 
	 * @returns
	 * Deep copy zadane zice.
	 * 
	 */
	ISchemaWire copyCtor();
	
	
	/**
	 * Vraca ime zice.
	 * 
	 * @return
	 * Jedinstveno ime zice za
	 * koje casing nije vazan.
	 */
	Caseless getName();
	

	/**
	 * Za dohvat parametara komponente,
	 * npr. imena komponente, kasnjenja
	 * komponente, itd.
	 * 
	 * @return
	 * Objekt navedenog tipa koji se ponasa kao
	 * string-object bazirana kolekcija.
	 */
	IParameterCollection getParameters();
	
	
	
	/**
	 * Vraca listu svih racvalista zice.
	 * 
	 * @return
	 * Lista koordinata svih racvalista.
	 * 
	 */
	List<XYLocation> getNodes();
	
	
	/**
	 * Vraca listu svih segmenata zice.
	 * 
	 * 
	 * @return
	 * Lista segmenata.
	 * 
	 */
	List<WireSegment> getSegments();
	
	
	/**
	 * Vraca objekt za
	 * iscrtavanje komponente.
	 * 
	 * @see IWireDrawer
	 */
	IWireDrawer getDrawer();
	
	
	/**
	 * Vraca minimalni pravokutnik unutar
	 * kojeg stane zica.
	 * 
	 * @return
	 * Bounding box, pravokutnik!
	 */
	Rect2d getBounds();
	
	
	/**
	 * Dodaje segment zice. Pritom
	 * po potrebi dodaje cvorove na
	 * mjesta gdje je to potrebno.
	 * 
	 * @param segment
	 * @return
	 * Vraca true ako je segment uspjesno dodan,
	 * a false u protivnom (npr. radi overlapa).
	 */
	boolean insertSegment(WireSegment segment);
	
	/**
	 * Brise iz liste segmenata navedeni segment.
	 * Pritom po potrebi mice cvorove koji postaju
	 * suvisni.
	 * 
	 * @param segment
	 * @return
	 * Vraca false ako segment nije naden.
	 */
	boolean removeSegment(WireSegment segment);
	
	/**
	 * Vraca segmente zice na danoj koordinati.
	 * @param x
	 * @param y
	 * @return
	 * Null ako nema segmenata na toj koordinati.
	 */
	Set<WireSegment> segmentsAt(int x, int y);
	
}








