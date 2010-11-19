package hr.fer.zemris.vhdllab.applets.texteditor.misc;

/**
 * Sucelje kojim je moguce dojavljivati da se je dogodila
 * nekakva promjena.
 * 
 * @author marcupic
 */
public interface ModificationListener {
	/**
	 * Poziva se kada se je dogodila promjena.
	 */
	void contentModified();
}
