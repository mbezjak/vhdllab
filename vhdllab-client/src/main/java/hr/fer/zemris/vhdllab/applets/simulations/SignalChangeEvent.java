package hr.fer.zemris.vhdllab.applets.simulations;

/**
 * Pomocna struktura koja predstavlja promjenu jednog signala na zadanu vrijednost.
 * 
 * @author marcupic
 */
public class SignalChangeEvent {
	
	/**
	 * Vrijednost na koju je signal promijenjen.
	 */
	private String value;
	/**
	 * Trenutak u kojem se je dogodila promjena.
	 */
	private long timestamp;
	
	/**
	 * @param value vrijednost na koju je signal promijenjen
	 * @param timestamp trenutak u kojem se je dogodila promjena
	 */
	public SignalChangeEvent(String value, long timestamp) {
		super();
		this.value = value;
		this.timestamp = timestamp;
	}

	/**
	 * Vrijednost na koju je signal promijenjen.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Trenutak u kojem se je dogodila promjena.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return "("+timestamp+","+value+")";
	}
}
