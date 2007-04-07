package hr.fer.zemris.vhdllab.applets.schema2.misc;

import java.util.HashMap;
import java.util.Map;


/**
 * Pomocna kolekcijica koja je handy
 * u nekim slucajevima.
 * 
 * @author Axel
 *
 */
public class InfoMap {
	private Map<String, Object> map;
	
	public InfoMap() {
		map = new HashMap<String, Object>();
	}
	
	
	/**
	 * 
	 * @param key
	 * Kljuc.
	 * 
	 * @return
	 * Objekt ako postoji zapis ili
	 * null ako ne postoji.
	 * Moze vratiti null i ako je
	 * netko pohranio null pod tim kljucem.
	 * 
	 */
	public Object get(String key) {
		return map.get(key);
	}
	
	/**
	 * Dodaje zapis ili prepisuje postojeci.
	 * 
	 * @param key
	 * Kljuc.
	 * @param object
	 * Objekt.
	 */
	public void set(String key, Object object) {
		map.put(key, object);
	}
}



