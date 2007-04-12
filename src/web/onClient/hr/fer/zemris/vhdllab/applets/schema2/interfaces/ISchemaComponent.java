package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;



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
	String getTypeName();
	
	
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
	String getName();
	
	
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
	 * Vraca visinu komponente na shemi.
	 * 
	 * @return
	 */
	int getHeight();
	
	
	/**
	 * Vraca sirinu komponente na shemi.
	 * 
	 * @return
	 */
	int getWidth();
	
	
	/**
	 * Vraca objekt koji ce generirati dijelove
	 * VHDL koda.
	 * 
	 * @return
	 * Objekt koji ce generirati dijelove koda.
	 */
	IVHDLSegmentProvider getVHDLSegmentProvider();
	
	
	
}

















