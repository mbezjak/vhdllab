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
	 * Vraca ime instance sklopa.
	 * OPREZ: Ova metoda je samo POKRATA
	 * za dohvat imena preko kolekcije 
	 * parametara koju vraca getParameters().
	 * 
	 * @return
	 * Ime instance sklopa - ista se vrijednost
	 * u svakom trenutku MORA dobiti sljedecim
	 * pozivom:
	 * 
	 * getParameters().getValue(ParamKeys.NAME);
	 * ili
	 * getParameters().getValue("NAME");
	 */
	String getName();
	
	
	/**
	 * Vraca ime sucelja, tj. ime tipa
	 * same komponente.
	 * OPREZ: Ova metoda je samo POKRATA
	 * za dohvat imena entity bloka (tipa komponente)
	 * preko kolekcije parametara koju vraca getParameters().
	 *  
	 * @return
	 * Ime tipa sklopa - ista se vrijednost
	 * u svakom trenutku MORA dobiti sljedecim
	 * pozivom:
	 * 
	 * getParameters().getValue(ParamKeys.ENTITY_NAME);
	 * ili
	 * getParameters().getValue("ENTITY_NAME");
	 */
	String getEntityName();
	
	
	/**
	 * Vraca jedan objekt tipa
	 * CircuitInterface koji opisuje sucelje
	 * sklopa.
	 * OPREZ: Pritom je bitno naglasiti da 
	 * metoda getEntityName() vraca istu 
	 * vrijednost kao i istoimena metoda
	 * objekta koji vraca getCircuitInterface.
	 * 
	 * U protivnom je interface ISchemaComponent
	 * lose implementiran.
	 * 
	 * @return
	 * Sucelje sklopa.
	 */
	CircuitInterface getCircuitInterface(); 
}










