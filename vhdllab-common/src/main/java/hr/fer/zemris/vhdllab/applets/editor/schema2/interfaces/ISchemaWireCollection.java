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

import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Rect2d;

import java.util.Set;



/**
 * Sucelje za skup zica koje se nalaze
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaWireCollection extends Iterable<ISchemaWire> {
	
	public static final int NO_WIRE = -1;
	
	/**
	 * Dohvaca zicu zadanog imena.
	 * 
	 * @param wireName
	 * Jedinstveni String identifikator
	 * zice (signala).
	 * 
	 * @return
	 * Vraca zicu ciji je jedinstveni
	 * identifikator zadano ime, ili null
	 * ako takva ne postoji.
	 * Pritom ime nije dio IParameterCollection,
	 * tj. sama zica ne zna svoje ime.
	 * Ime je jedinstveni identifikator po kojem
	 * je moguce dohvacati zice.
	 * 
	 */
	ISchemaWire fetchWire(Caseless wireName);
	
	
	/**
	 * Odreduje da li postoji zadano ime.
	 * 
	 * @param wireName
	 * Ime zice.
	 * 
	 * @return
	 * True ako zica zadanog imena
	 * postoji u kolekciji, false inace.
	 * 
	 */
	boolean containsName(Caseless wireName);
	
	
	/**
	 * Dohvaca zicu na zadanim koordinatama.
	 * 
	 * @param x
	 * @param y
	 * @param dist
	 *  
	 * @return
	 * Vraca prvu zicu ako takva postoji na zadanim
	 * koordinatama ili je udaljena za dist od njih.
	 * Ako ista ne postoji, vraca se null.
	 * 
	 */
	ISchemaWire fetchWire(int x, int y, int dist);
	
	
	/**
	 * Vraca skup svih zica na danoj koordinati.
	 * @param x
	 * @param y
	 * @return
	 * Null ako nema zica na navedenoj koordinati.
	 */
	Set<ISchemaWire> fetchAllWires(int x, int y);
	
	
	
	/**
	 * Za zicu danog imena vraca pravokutnik
	 * koji je obuhvaca.
	 * 
	 * @param wireName
	 * @return
	 * Minimalni pravokutnik koji obuhvaca
	 * zicu, a null zica tog imena ne postoji.
	 */
	Rect2d getBounds(Caseless wireName);
	
	
	
	/**
	 * Odreduje da li postoji zica
	 * na koordinatama.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * Vraca zicu ako takva postoji na zadanim
	 * koordinatama ili je udaljena za dist od njih.
	 * Ako ista ne postoji, vraca se null.
	 * 
	 */
	boolean containsAt(int x, int y, int dist);
	
	
	/**
	 * Dodaje zicu u kolekciju.
	 * 
	 * @param wire
	 * Zica jedinstvenog imena.
	 * @throws DuplicateKeyException
	 * Ako postoji zica tog imena u kolekciji.
	 * @throws OverlapException
	 * Ako dolazi do preklapanja s postojecim zicama.
	 */
	void addWire(ISchemaWire wire) throws DuplicateKeyException, OverlapException;
	
	/**
	 * Mice zicu iz kolekcije.
	 * 
	 * @param wireName
	 * Ime zice.
	 * @throws UnknownKeyException
	 * Ako zica (signal) tog imena ne postoji
	 * u kolekciji.
	 */
	void removeWire(Caseless wireName) throws UnknownKeyException;
	
	/**
	 * Vraca udaljenost do navedene zice.
	 * 
	 * @param name
	 * @param xfrom
	 * X lokacija od koje se mjeri udaljenost do zice.
	 * @param yfrom
	 * Y lokacija od koje se mjeri udaljenost do zice.
	 * @return
	 * Ako zica navedenog imena ne postoji, vraca se
	 * ISchemaWireCollection.NO_WIRE.
	 * U protivnom se vraca udaljenost do zice,
	 * ili 0 u slucaju da je (xfrom, yfrom) na samoj
	 * zici.
	 * Iznimno, vraca Integer.MAX_VALUE, ako zica
	 * nema segmenata (rubni slucaj).
	 */
	int distanceTo(Caseless name, int xfrom, int yfrom);
	
	/**
	 * Vraca skup imena zica na shemi,
	 * preko kojeg je moguce iterirati
	 * po zicama.
	 * 
	 */
	Set<Caseless> getWireNames();
	
	
	/**
	 * Brise sve zice iz kolekcije.
	 *
	 */
	void clear();
	
	
}













