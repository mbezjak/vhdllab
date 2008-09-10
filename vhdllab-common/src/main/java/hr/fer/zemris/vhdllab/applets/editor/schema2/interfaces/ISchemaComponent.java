package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.ComponentWrapper;

import java.util.Iterator;
import java.util.List;



/**
 * Sucelje koje opisuje bilo koji sklop (komponentu)
 * na shemi.
 * BITNO: Svaki ISchemaComponent za potrebe deserijalizacije
 * mora imati jedan konstruktor koji kao jedini parametar
 * prima objekt tipa ComponentWrapper.
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
	 * Informacija o ovome je tipicno pohranjena
	 * unutar kolekcije parametara.
	 * 
	 * @return
	 */
	EOrientation getComponentOrientation();
	
	/**
	 * Vraca tip komponente.
	 * Najvise se koristi za odredivanje
	 * da li komponenta modelira ulaz i izlaz,
	 * ili je komponenta standardna.
	 * 
	 * @return
	 */
	EComponentType getComponentType();
	
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
	 * Dohvaca port po imenu.
	 * 
	 * @param name
	 * @return
	 * Null ako ne postoji port tog imena.
	 */
	SchemaPort getSchemaPort(Caseless name);
	
	
	/**
	 * Vraca sve fizicke portove na komponenti.
	 * 
	 * @return
	 * Genericka lista unutar koje
	 * su portovi.
	 */
	List<SchemaPort> getSchemaPorts();
	
	
	/**
	 * Vraca broj portova na komponenti.
	 * 
	 * @return
	 */
	int schemaPortCount();
	
	/**
	 * Dohvaca port pod zadanim indeksom.
	 * 
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	Port getPort(int index);
	
	/**
	 * Dohvaca orijentaciju porta pod zadanim indeksom.
	 * 
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	EOrientation getPortOrientation(int index);
	
	/**
	 * Postavlja port pod zadanim indeksom,
	 * dodajuci (i brisuci) pritom
	 * odgovarajuce pinove.
	 * 
	 * @param index
	 * @param port
	 */
	void setPort(int index, Port port);
	
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
	 * Moze vratiti null ako je komponenta samo
	 * surogat u smislu da nema svoj vhdl opis,
	 * vec samo sluzi kao olaksica u modeliranju
	 * sklopa u schematicu (primjer - ui komponente).
	 */
	String getCodeFileName();
	
	/**
	 * Da li je komponenta genericka - da li
	 * u svom sucelju sadrzi GENERIC blok, i samim
	 * time generic parametre.
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
	 * Vraca listu schemaportova (pinova) na koju
	 * se odnosi Port pod zadanim indeksom.
	 * 
	 * @param portIndex
	 * @throws IndexOutOfBoundsException
	 * @return
	 */
	List<SchemaPort> getRelatedTo(int portIndex);
	
	
	/**
	 * Sluzi za iteriranje kroz portove koji
	 * se pojavljuju u sucelju sklopa.
	 *  
	 */
	Iterator<Port> portIterator();
	
	
	/**
	 * Sluzi za iteriranje kroz fizicke portove
	 * komponente u shemi (pinove).
	 * 
	 * @return
	 */
	Iterator<SchemaPort> schemaPortIterator();
	
	
	/**
	 * Odreduje da li je komponenta u invalidated stanju.
	 * Komponenti koja je u invalidated stanju ne moze se
	 * pristupati, ne moze se dohvatiti kolekcija parametara,
	 * portova, itd.
	 * Tipicne implementacije komponente vracaju true. Posebna
	 * implementacija ove komponente vraca false, a nju se u
	 * shemu stavlja automatski ako se ustanovi da se sucelje
	 * neke od komponenti u shemi promijenilo (a uzrok promjene
	 * je izvan sheme).
	 * 
	 * @return
	 */
	boolean isInvalidated();
	
}

















