package hr.fer.zemris.vhdllab.applets.schema2.interfaces;


/**
 * Sucelje koje opisuje tvornice
 * koje stvaraju objekte.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponentFactory {
	
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
	 * @param entityBlock
	 * Entity blok odreduje koje ce ulaze i izlaze
	 * imati pojedini sklop na shemi.
	 * @return
	 * Vraca jedan objekt tipa ISchemaComponent.
	 */
	ISchemaComponent generateSchemaComponent(String genericBlock, String entityBlock);
}
