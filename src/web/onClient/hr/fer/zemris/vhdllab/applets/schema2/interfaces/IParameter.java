package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidParameterValueException;




/**
 * Definira izgled parametra za
 * za zice i/ili sklopove unutar
 * schematica.
 * IParameterCollection
 * vraca objekte koji implementiraju
 * ovo sucelje.
 * 
 * @author Axel
 *
 */
public interface IParameter {
	/**
	 * Za odredivanje tipa parametra.
	 * 
	 * @return
	 * Vraca vrijednost iz navedene
	 * enumeracije koja odreduje tip
	 * parametra.
	 */
	EParamTypes getType();
	
	/**
	 * Za dobivanje vrijednosti
	 * parametra.
	 * 
	 * @return
	 * Vraca vrijednost vezanu uz
	 * parametar.
	 * Po potrebi je Object moguce
	 * castati u potrebni tip.
	 */
	Object getValue();
	
	/**
	 * Postavlja vrijednost parametra.
	 * 
	 * @throws
	 * Navedenu iznimku u slucaju da proslijedena
	 * vrijednost nije tipa kojeg je sam parametar.
	 * Npr. nedozvoljeno je proslijediti String,
	 * ako getType() vraca EParameterTypes.INTEGER.
	 * 
	 * @param value
	 * Vrijednost na koju parametar
	 * treba postaviti.
	 */
	void setValue(Object value) throws InvalidParameterValueException;
	
	
	/**
	 * Odreduje da li je moguce isparsirati vrijednost
	 * parametra na temelju proslijedenog stringa,
	 * a ne objekta.
	 * 
	 * @return
	 * Vraca true ako je vrijednost parametra
	 * moguce postavljati proslijedivanjem 
	 * stringa.
	 */
	boolean isParsable();
	
	
	/**
	 * Ako isParsable() vraca true onda je
	 * ovom metodom moguce postaviti vrijednost
	 * parametra proslijedivanjem stringa.
	 * Ako
	 * 
	 * @throws
	 * Navedenu iznimku ako isParsable() vraca false,
	 * ili ako nije moguce isparsirati tu vrijednost
	 * (npr. 0.14e12dcvszdfs nije moguce isparsirati
	 * ako je vrijednost cjelobrojnog tipa) ili ako
	 * je vrijednost moguce isparsirati ali IParameterConstraint
	 * ne dozvoljava navedenu vrijednost. 
	 * 
	 * @param stringValue
	 * String vrijednost parametra koja se
	 * sama isparsira ako je to moguce.
	 */
	void setWithString(String stringValue) throws InvalidParameterValueException;
	
	
	/**
	 * Sluzi za odredivanje eventualnih ogranicenja
	 * na vrijednosti parametra.
	 * 
	 * @return
	 * Vraca objekt navedenog tipa na temelju kojeg
	 * je moguce odrediti ogranicenja.
	 * Ako ogranicenja nema NECE vratiti null, vec
	 * IParameterConstraint koji dozvoljava sve vrijednosti.
	 * 
	 * @see IParameterConstraint
	 * 
	 */
	IParameterConstraint getConstraint();
}












