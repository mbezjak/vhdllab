package hr.fer.zemris.vhdllab.applets.simulations;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache za objekte {@link SignalChangeEvent}.
 * 
 * @author marcupic
 */
public class SignalChangeEventCache {
	
	/**
	 * Mapa koja čuva objekte.
	 */
	private Map<String, SignalChangeEvent> cache;
	
	/**
	 * Mapa koja predstavlja cache za vrijednosti, tako da nemamo
	 * višestruke kopije stringova u memoriji.
	 */
	private Map<String, String> valueCache;
	
	/**
	 * Konstruktor.
	 */
	public SignalChangeEventCache() {
		cache = new HashMap<String, SignalChangeEvent>(1000);
		valueCache = new HashMap<String, String>();
	}
	
	/**
	 * Dohvat objekta iz cache-a. Ako takav objekt ne postoji, biti
	 * će stvoren i ubačen u cache.
	 * 
	 * @param value vrijednost signala
	 * @param time vrijeme promjene
	 * @return traženi objekt
	 */
	public SignalChangeEvent get(String value, long time) {
		String val = valueCache.get(value);
		if(val==null) {
			val = value;
			valueCache.put(val, val);
		}
		value = val;
		String key = value + "\t" + time;
		SignalChangeEvent ev = cache.get(key);
		if(ev!=null) return ev;
		ev = new SignalChangeEvent(value, time);
		cache.put(key, ev);
		return ev;
	}
}
