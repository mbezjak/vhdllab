package hr.fer.zemris.vhdllab.applets.schema2.misc;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IQueryResult;

import java.util.HashMap;
import java.util.Map;





/**
 * Objekt koji je rezultat upita.
 * 
 * @author brijest
 *
 */
public class QueryResult implements IQueryResult {
	/* static fields */

	/* private fields */
	private Map<String, Object> infomap;
	private boolean success;
	

	/* ctors */
	
	/**
	 * Konstruira rezultat upita.
	 * 
	 */
	public QueryResult(boolean isSuccessful) {
		infomap = new HashMap<String, Object>();
		success = isSuccessful;
	}
	
	/**
	 * Konstruira rezultat upita, postavljajuci mu
	 * uspjesnost na true, i pritom dodajuci jedan
	 * novi zapis u infomap-u rezultata upita.
	 * 
	 * @param key
	 * @param value
	 */
	public QueryResult(String key, Object value) {
		infomap = new HashMap<String, Object>();
		success = true;
		
		infomap.put(key, value);
	}
	

	/* methods */
	
	public Object get(String key) {
		return infomap.get(key);
	}
	
	public void put(String key, Object value) {
		infomap.put(key, value);
	}
	
	public boolean isSuccessful() {
		return success;
	}

	
	

}















