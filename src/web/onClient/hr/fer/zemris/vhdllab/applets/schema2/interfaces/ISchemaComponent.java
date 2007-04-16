package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.util.Set;



/**
 * Sucelje koje opisuje bilo koji sklop (komponentu)
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponent extends ISerializable {
	
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
	 * CParamKeys.NAME. Ova metoda je pokrata
	 * za dohvacanje te vrijednosti.
	 * 
	 */
	Caseless getName();
	
	
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
	 * Dohvaca port po imenu.
	 * 
	 * @param name
	 * @return
	 * Port komponente, ili
	 * null ako ne postoji port
	 * tog imena.
	 */
	SchemaPort getSchemaPort(Caseless name);
	
	/**
	 * Vraca imena svih portove na komponenti.
	 * 
	 * @return
	 * Genericka lista unutar koje
	 * su imena portova.
	 */
	Set<String> getAllPorts();
	
	
	/**
	 * Vraca objekt koji ce generirati dijelove
	 * VHDL koda.
	 * 
	 * @return
	 * Objekt koji ce generirati dijelove koda.
	 */
	IVHDLSegmentProvider getVHDLSegmentProvider();
	
	
	
	
}

















