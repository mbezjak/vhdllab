package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.serialization.ComponentWrapper;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.util.Iterator;
import java.util.List;



/**
 * Sucelje koje opisuje bilo koji sklop (komponentu)
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponent {
	
	/**
	 * Parametar koji bi trebao biti prisutan u svakoj kolekciji
	 * parametara, a odnosi se na ime komponente.
	 */
	public static final String KEY_NAME = "Name";
	
	/**
	 * Odnosi se na orijentaciju komponente.
	 */
	public static final String KEY_ORIENTATION = "Orientation";
	
	
	/**
	 * Virtualni copy konstruktor.
	 * 
	 * @return
	 * Vraca jedan objekt tipa ISchemaComponent,
	 * cija je klasa upravo ona klasa koja
	 * implementira ovu metodu.
	 * Pritom se svi private clanovi moraju
	 * prekopirati u novonastalu klasu (i to
	 * NE kao copy by reference, vec kao
	 * copy by value). Dakle, vraca se deep copy
	 * objekta.
	 * 
	 * Ova metoda se ne zove u normalnim okolnostima,
	 * vec iskljucivo ako:
	 * 1) Netko kopira postojecu komponentu na shemi.
	 * 2) Objekt ove klase koristi se kao prototip u
	 *    ISchemaPrototypeCollection.
	 *    
	 * Za kraj, ovaj konstruktor kopije NECE prekopirati
	 * informaciju o tome na koje zice je komponenta
	 * spojena - novonastali SchemaPort-ovi nece biti
	 * spojeni ni na sto.
	 * 
	 */
	ISchemaComponent copyCtor();
	
	
	/**
	 * Za dohvat parametara komponente,
	 * npr. broja portova, kasnjenja
	 * komponente, itd.
	 * Jedan clan koji uvijek ovdje mora postojati
	 * jest pod kljucem CParamKeys.NAME - pri
	 * stvaranju sklopa isti se mora unijeti.
	 * 
	 * @return
	 * Objekt navedenog tipa koji se ponasa kao
	 * string-object bazirana kolekcija.
	 */
	IParameterCollection getParameters();
	
	
	/**
	 * Vraca ime sucelja, tj. ime tipa
	 * same komponente.
	 *  
	 * @return
	 * Ime tipa sklopa koje je jedinstveno unutar schematica
	 * za taj tip sklopa.
	 * 
	 */
	Caseless getTypeName();
	
	
	/**
	 * Vraca identifier koji odreduje ime
	 * instance komponente.
	 * 
	 * @return
	 * Ime instance komponente. U parameter
	 * kolekciji uvijek postoji identifier
	 * ISchemaComponent.NAME. Ova metoda je pokrata
	 * za dohvacanje te vrijednosti.
	 * 
	 */
	Caseless getName();
	
	/**
	 * Postavlja ime instance komponente.
	 * Pokrata za postavljanje odgovarajuce
	 * vrijednosti u parameter kolekciji.
	 * 
	 * @param name
	 */
	void setName(Caseless name);
	
	
	/**
	 * Vraca jedan objekt tipa
	 * CircuitInterface koji opisuje sucelje
	 * sklopa.
	 * OPREZ: Pritom je bitno naglasiti da 
	 * metoda getTypeName() vraca istu 
	 * vrijednost kao i getEntityName() metoda
	 * objekta koji vraca getCircuitInterface().
	 * 
	 * U protivnom je interface ISchemaComponent
	 * lose implementiran i o tome se brine
	 * programer, a ne kompajler, stoga OPREZ!
	 * 
	 * @return
	 * Sucelje sklopa.
	 */
	CircuitInterface getCircuitInterface();
	
	
	/**
	 * Dohvaca orijentaciju komponente.
	 * 
	 * @return
	 */
	EOrientation getComponentOrientation();
	
	
	/**
	 * Postavlja orijentaciju komponente.
	 * 
	 * @param orient
	 */
	void setComponentOrientation(EOrientation orient);
	
	
	/**
	 * Vraca visinu komponente na shemi.
	 * 
	 * @return
	 * Vraca visinu komponente. Ako se promijeni
	 * orijentacija komponente, mogu
	 * se zamijeniti visina i sirina.
	 */
	int getHeight();
	
	
	/**
	 * Vraca sirinu komponente na shemi.
	 * 
	 * @return
	 * Sirinu komponente. Ako se promijeni
	 * orijentacija komponente, mogu
	 * se zamijeniti visina i sirina.
	 */
	int getWidth();
	
	
	/**
	 * Dohvaca najblizi port na komponenti, ovisno
	 * o orijentaciji, naravno.
	 * 
	 * @param xoffset
	 * Odmak od gornjeg lijevog ruba komponente
	 * po x-osi.
	 * @param yoffset
	 * Odmak od gornjeg lijevog ruba komponente
	 * po y-osi.
	 * @param dist
	 * Maksimalna udaljenost od porta do specificiranih
	 * koordinata.
	 * @return
	 * Null ako takav ne postoji, a port inace.
	 */
	SchemaPort getSchemaPort(int xoffset, int yoffset, int dist);
	
	
	/**
	 * Dohvaca port po rednom broju.
	 * 
	 * @param name
	 * @return
	 * Port komponente, ili
	 * null ako ne postoji port
	 * tog indeksa.
	 */
	SchemaPort getSchemaPort(int index);
	
	
	/**
	 * Vraca sve portove na komponenti.
	 * 
	 * @return
	 * Genericka lista unutar koje
	 * su imena portova.
	 */
	List<SchemaPort> getSchemaPorts();
	
	
	/**
	 * Vraca broj portova na komponenti.
	 * 
	 * @return
	 */
	int portCount();
	
	
	/**
	 * Vraca objekt koji ce generirati dijelove
	 * VHDL koda.
	 * 
	 * @return
	 * Objekt koji ce generirati dijelove koda.
	 * Vraca null ako ne treba nista izgenerirati.
	 */
	IVHDLSegmentProvider getVHDLSegmentProvider();
	
	
	/**
	 * Za dohvat kategorije unutar koje
	 * ce biti smjesten sklop.
	 * Ovo je bitno za eventualni
	 * view (gui) jer su sklopovi
	 * logicki odvojeni u skupine.
	 * 
	 * @return
	 * Vraca ime kategorije u koju
	 * ce biti smjesten sklop (komponenta).
	 */
	String getCategoryName();
	
	
	/**
	 * Vraca objekt za iscrtavanje.
	 * 
	 * @return
	 * Vidi opis ovog sucelja.
	 */
	IComponentDrawer getDrawer();
	
	/**
	 * Vraca ime datoteke u kojoj se nalazi vhdl
	 * opis komponente.
	 * 
	 * @return
	 */
	String getCodeFileName();
	
	/**
	 * Da li je komponenta genericka - da li
	 * u svom sucelju sadrzi GENERIC blok.
	 * 
	 * @return
	 */
	boolean isGeneric();
	
	
	/**
	 * Koristi se pri deserijalizaciji.
	 * 
	 * @param compwrap
	 */
	void deserialize(ComponentWrapper compwrap);
	
	
	/**
	 * Sluzi za iteriranje kroz portove koji
	 * se pojavljuju u sucelju.
	 * 
	 * @see <code>getSchemaPorts()</code> 
	 */
	Iterator<Port> portIterator();
	
	/**
	 * Sluzi za iteriranje kroz fizicke portove
	 * komponente u shemi.
	 * 
	 * @return
	 */
	Iterator<SchemaPort> schemaPortIterator();
	
}

















