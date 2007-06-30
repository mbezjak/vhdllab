package hr.fer.zemris.vhdllab.applets.schema2.enums;



/**
 * Opisuje tip komponente.
 * 
 * @author brijest
 *
 */
public enum EComponentType {

	/**
	 * Komponente koje su predefinirane unutar
	 * konfiguracijske datoteke, i uvijek su dostupne.
	 * Mogu sadrzavati generic parametre.
	 */
	PREDEFINED,
	/**
	 * Komponente koje su korisnicki definirane,
	 * a konstruirane na temelju entity bloka.
	 * Dostupne su ako su unutar projekta.
	 * Ne sadrze generic parametre.
	 */
	USER_DEFINED,
	/**
	 * Surogat komponente koje sluze kao ulaz i izlaz
	 * iz modeliranog sklopa.
	 * Informacija da je komponenta IN_OUT bitna je na
	 * vise mjesta, npr. kod serijalizacije, ili kod dinamickog
	 * odredivanja portova.
	 */
	IN_OUT,
	/**
	 * Rezervirano za buduce dorade.
	 */
	NON_BASIC
	
	
}





