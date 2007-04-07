package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.vhdl.model.Type;



/**
 * Sucelje koje opisuje bilo koju zicu (signal)
 * na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaWire extends ISerializable {

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
	 * Vraca ime zice (signala).
	 * OPREZ: Ova metoda je samo POKRATA
	 * za dohvat imena preko kolekcije 
	 * parametara koju vraca getParameters().
	 * 
	 * @return
	 * Ime zice (signala) - ista se vrijednost
	 * u svakom trenutku MORA dobiti sljedecim
	 * pozivom:
	 * 
	 * getParameters().getValue(ParamKeys.NAME);
	 * ili
	 * getParameters().getValue("NAME");
	 */
	String getName();
	
	
	/**
	 * Za dohvat tipa signala.
	 * 
	 * @see Type
	 * 
	 * @return
	 * Vraca tip signala, treba
	 * prouciti navedeni Mirin
	 * interface.
	 * 
	 */
	Type getType();
}








