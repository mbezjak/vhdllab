package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;

import java.util.List;




/**
 * Ovo sucelje opisuje objekte koji obavljaju
 * neki upit nad shemom.
 * Implementacija ovog sucelja svojim akcijama
 * NECE promijeniti stanje ISchemaInfo objekta.
 * 
 * Klase koji implementiraju ovo sucelje moraju
 * OBAVEZNO implementirati equals na nacin da
 * se uzimaju u obzir SVA polja <b>koja odreduju upit</b>,
 * inace ce doci do povrata krivo cacheiranih informacija.
 * Ne treba, stovise, ne smije se uzimati u obzir pomocna
 * polja koja ne odreduju prirodu upita.
 * 
 * Takoder je nuzno implementirati hashCode().
 * 
 * @author brijest
 *
 */
public interface IQuery {

	
	/**
	 * Odgovara na pitanje da li je upit cacheable.
	 * Ako je upit cacheable, onda ako se upit izvede, i ako na shemi nije 
	 * bilo promjena (na koje je osjetljiv ovaj upit) i ako je sljedeci
	 * upit potpuno isti kao i ovaj (equals vraca true) onda sljedeci
	 * upit NECE biti izveden ponovno, vec ce se vratiti rezultat iz
	 * cachea upita.
	 * 
	 */
	boolean isCacheable();
	
	/**
	 * Vraca ime upita.
	 */
	String getQueryName();
	
	/**
	 * Lista tipa promjena na koje je osjetljiv ovaj upit.
	 * Ako se desi promjena ovog tipa, upit ce biti izbrisan iz cachea.
	 * Ne smije biti mijenjana!!
	 * 
	 */
	List<EPropertyChange> getPropertyDependency();
	
	/**
	 * Obavlja upit i vraca objekt koji opisuje uspjesnost upita,
	 * i sadrzi informacije vezane uz upit.
	 * 
	 * @param info
	 * Objekt koji IQuery NECE promijeniti.
	 */
	IQueryResult performQuery(ISchemaInfo info);
	
	boolean equals(Object o);
	
	int hashCode();
	
}









