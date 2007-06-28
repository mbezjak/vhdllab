package hr.fer.zemris.vhdllab.applets.schema2.interfaces;



/**
 * Sluzi za generiranje djelica VHDL koda
 * kad se gradi citav strukturni kod.
 * Npr. vraca dio vezan uz port map
 * koji ce biti integriran u kod.
 * Takoder, vraca djelice VHDL koda potrebne
 * za eventualnu definiciju signala koje potrebuje
 * komponenta.
 * 
 * Podrazumijeva se da je getSignalDefinitions()
 * UVIJEK pozvan prije getInstantiation() i da
 * getInstantion() nikad nije pozvan bez da je
 * prethodno pozvan getSignalDefinitions().
 * 
 * @author Axel
 *
 */
public interface IVHDLSegmentProvider {
	/**
	 * Vraca kod za signale koji su potrebni
	 * za prospajanje zica na komponentu.
	 * 
	 * @param info
	 * Info sluzi kako bi se u slucaju koristenja
	 * pomocnih signala razrijesili konflikti imena.
	 * @return
	 */
	String getSignalDefinitions(ISchemaInfo info);
	
	/**
	 * Vraca linije koda vezanu uz instanciranje
	 * komponente i prospajanje zica.
	 * 
	 * @param info
	 * Info sluzi kako bi se u slucaju koristenja
	 * pomocnih signala razrijesili konflikti imena.
	 * @return
	 */
	String getInstantiation(ISchemaInfo info);
}









