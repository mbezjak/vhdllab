package hr.fer.zemris.vhdllab.applets.simulations;

import java.util.Map;

/**
 * Razred koji cuva sve promjene po svim signalima.
 * 
 * @author marcupic
 */
public class SignalChangeTracker {
	
	/**
	 * Mapa koja za svaki signal sadrži polje promjena
	 */
	private Map<String, SignalChangeEvent[]> signalChangeMap;

	/**
	 * Konstruktor. Predana mapa ne smije se izvana mijenjati.
	 * 
	 * @param signalChangeMap mapa promjena
	 */
	public SignalChangeTracker(Map<String, SignalChangeEvent[]> signalChangeMap) {
		this.signalChangeMap = signalChangeMap;
	}

	/**
	 * Vraca polje svih promjena po signalu.
	 * 
	 * @param signalKey identifikator signala
	 * @return polje sa svim promjenama
	 */
	public SignalChangeEvent[] get(String signalKey) {
		return signalChangeMap.get(signalKey);
	}
}
