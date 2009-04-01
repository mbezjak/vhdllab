package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidParameterValueException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Time;




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
	 * Stvara kopiju ovog parametra.
	 * Koristi se kod kopiranja komponenti.
	 * 
	 */
	IParameter copyCtor();
	
	/**
	 * Dohvaca ime parametra.
	 * 
	 */
	String getName();
	
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
	 * Sam obavlja cast ako je
	 * tog tipa i vraca vrijednost.
	 * 
	 * @throws ClassCastException
	 * Ako vrijednost nije tog navedenog
	 * tipa, nece se parsirati ili convertati
	 * vec ce se baciti exception.
	 */
	Integer getValueAsInteger() throws ClassCastException;
	
	/**
	 * Sam obavlja cast ako je
	 * tog tipa i vraca vrijednost.
	 * 
	 * @throws ClassCastException
	 * Ako vrijednost nije tog navedenog
	 * tipa, nece se parsirati ili convertati
	 * vec ce se baciti exception.
	 */
	String getValueAsString() throws ClassCastException;
	
	/**
	 * Sam obavlja cast ako je
	 * tog tipa i vraca vrijednost.
	 * 
	 * @throws ClassCastException
	 * Ako vrijednost nije tog navedenog
	 * tipa, nece se parsirati ili convertati
	 * vec ce se baciti exception.
	 */
	Double getValueAsDouble() throws ClassCastException;
	
	/**
	 * Sam obavlja cast ako je
	 * tog tipa i vraca vrijednost.
	 * 
 	 * @throws ClassCastException
	 * Ako vrijednost nije tog navedenog
	 * tipa, nece se parsirati ili convertati
	 * vec ce se baciti exception.
	 */
	Boolean getValueAsBoolean() throws ClassCastException;
	
	/**
	 * Sam obavlja cast ako je
	 * tog tipa i vraca vrijednost.
	 * 
	 * @throws ClassCastException
	 * Ako vrijednost nije tog navedenog
	 * tipa, nece se parsirati ili convertati
	 * vec ce se baciti exception.
	 */
	Time getValueAsTime() throws ClassCastException;
	
	
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
	 * umjesto objekta.
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
	 * 
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
	void setAsString(String stringValue) throws InvalidParameterValueException;
	
	
	/**
	 * Odgovara na pitanje da li je konkretna
	 * vrijednost parsabilna.
	 * Primjer, za cjelobrojni integer je '5'
	 * parsabilno, ali '153sdag4' nije.
	 * 
	 * @param stringValue
	 * @return
	 * Uvijek vraca false ako isParsable() vraca false.
	 */
	boolean checkStringValue(String stringValue);
	
	
	/**
	 * Odgovara na pitanje da li je parametar
	 * genericki.
	 * Ako je parametar generickog tipa, tad ce
	 * se njegova vrijednost koristiti u 
	 * GENERIC MAP dijelu instanciranja komponente
	 * kojoj on pripada.
	 *  
	 */
	boolean isGeneric();
	
	/**
	 * Vraca string koji se koristi u GENERIC MAP
	 * dijelu instanciranja komponente u VHDL
	 * kodu.
	 * 
	 */
	String getVHDLGenericEntry();
	
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
	
	/**
	 * Vraca event pridruzen ovom parametru.
	 * 
	 */
	IParameterEvent getParameterEvent();
	
	/**
	 * Pridruzuje event parametru.
	 */
	void setParameterEvent(IParameterEvent event);
	
	/**
	 * Postavlja event parametru putem njegovog
	 * Java identifikatora.
	 * 
	 * @param string
	 */
	void setParameterEvent(String eventName);
	
	/**
	 * Vraca puno ime klase pohranjenog objekta.
	 * Koristi se kod serijalizacije.
	 * 
	 */
	String getValueClassName();
}












