package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;



/**
 * Definira sucelje klasa koje ce sadrzavati
 * opis entity bloka komponente koja se 
 * modelira u schematicu.
 * 
 * @author Axel
 *
 */
public interface ISchemaEntity {
	
	
	/**
	 * Vraca sucelje sklopa koji se modelira
	 * u schematicu - broj i vrstu portova.
	 * 
	 * @return
	 */
	CircuitInterface getCircuitInterface();
	
	
	/**
	 * Vraca mapu parametara komponente
	 * koja se modelira u schematicu.
	 * Na temelju ove kolekcije biti ce kasnije
	 * izgraden generic blok pri generiranju
	 * strukturnog VHDLa.
	 * 
	 * @return
	 */
	IParameterCollection getParameters();
	
	/**
	 * Postavlja parametre (vidi metodu
	 * getParameters()).
	 * 
	 * @param parameters
	 */
	void setParameters(IParameterCollection parameters);
	
	
}








