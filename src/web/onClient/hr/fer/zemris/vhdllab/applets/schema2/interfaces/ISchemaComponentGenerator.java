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
	 * @param entityBlock
	 * Entity blok odreduje koje ce ulaze i izlaze
	 * imati pojedini sklop na shemi.
	 * Takoder, na temelju generic dijela je moguce
	 * izgenerirati IParameterCollection za komponentu.
	 * 
	 * @return
	 * Vraca jedan objekt tipa ISchemaComponent.
	 */
	ISchemaComponent generateSchemaComponent(String entityBlock);
}








