package hr.fer.zemris.vhdllab.applets.schema2.interfaces;



/**
 * Interface koji definira serijalizaciju
 * i deserijalizaciju.
 * 
 * @author Axel
 *
 */
public interface ISerializable {
	/**
	 * Serijalizira objekt.
	 * 
	 * @return
	 * Vraca kod na temelju kojeg
	 * je moguce rekonstruirati objekt.
	 * 
	 */
	String serialize();
	
	/**
	 * Deserijalizira objekt.
	 * 
	 * @param code
	 * Kod na temelju kojeg se rekonstruira objekt.
	 */
	boolean deserialize(String code);
}
