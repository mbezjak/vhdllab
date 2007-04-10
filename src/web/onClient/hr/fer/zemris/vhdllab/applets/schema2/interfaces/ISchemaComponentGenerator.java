package hr.fer.zemris.vhdllab.applets.schema2.interfaces;


/**
 * Sucelje koje opisuje tvornice
 * koje stvaraju objekte.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponentGenerator {
	
	/**
	 * Kreira jedan objekt tipa
	 * ISchemaComponent na temelju specificiranog
	 * generic bloka i entity bloka.
	 * 
	 * @param genericBlock
	 * Generic blok odreduje koje ce parametre
	 * pojedina shema imati. Tvornica na temelju
	 * generic bloka (koji postuje pravila VHDLa)
	 * generira kolekciju parametara komponente.
	 * 
	 * @param entityBlock
	 * Entity blok odreduje koje ce ulaze i izlaze
	 * imati pojedini sklop na shemi.
	 * NAPOMENA: Entity blok takoder sadrzi podatak
	 * o nazivu tipa sklopa.
	 * U kolekciji parametara izgeneriranog objekta
	 * mora stoga biti prisutan parametar tipa
	 * "ENTITY_NAME" (CParamKeys.ENTITY_NAME),
	 * cija je vrijednost naziv tipa sklopa.
	 * 
	 * @return
	 * Vraca jedan objekt tipa ISchemaComponent.
	 */
	ISchemaComponent generateSchemaComponent(String genericBlock, String entityBlock);
}








