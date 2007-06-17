package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.List;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;



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
	 * Na temelju dijela ove kolekcije biti ce kasnije
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
	
	/**
	 * Postavlja portove za sucelje modelirane komponente.
	 * 
	 * @param ports
	 */
	public void setPorts(List<Port> ports);
	
	/**
	 * Dohvaca portove sucelja modelirane komponente.
	 * 
	 * @return
	 */
	public List<Port> getPorts();
	
	/**
	 * Brise portove i ne-defaultne parametre.
	 *
	 */
	void reset();
	
}








