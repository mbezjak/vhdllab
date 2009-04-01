package hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.List;



/**
 * Definira sucelje klasa koje ce sadrzavati
 * opis entity bloka komponente koja se 
 * modelira u schematicu.
 * 
 * @author Axel
 *
 */
public interface ISchemaEntity {
	
	public static final String KEY_NAME = "Name";
	
	
	/**
	 * Vraca sucelje sklopa koji se modelira
	 * u schematicu - broj i vrstu portova.
	 * 
	 */
	CircuitInterface getCircuitInterface(ISchemaInfo info);
	
	
	/**
	 * Vraca mapu parametara komponente
	 * koja se modelira u schematicu.
	 * Na temelju dijela ove kolekcije biti ce kasnije
	 * izgraden generic blok pri generiranju
	 * strukturnog VHDLa.
	 * 
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
	 * Vraca vrijednost uvijek prisutnog parametra pod kljucem KEY_NAME,
	 * koji oznacava ime entity-a.
	 */
	Caseless getName();
	
	/**
	 * Dohvaca portove sucelja modelirane komponente.
	 * 
	 */
	public List<Port> getPorts(ISchemaInfo info);
	
	/**
	 * Brise portove i ne-defaultne parametre.
	 *
	 */
	void reset();
	
}








