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
 * @author Axel
 *
 */
public interface IVHDLSegmentProvider {
	/**
	 * Bit ce opisano.
	 * 
	 * @return
	 */
	String getSignalDefinitions();
	
	/**
	 * Bit ce opisano.
	 * 
	 * @return
	 */
	String getPortMapDefinitions();
}
