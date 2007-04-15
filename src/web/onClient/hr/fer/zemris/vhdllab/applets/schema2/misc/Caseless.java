package hr.fer.zemris.vhdllab.applets.schema2.misc;




/**
 * U principu String za koji je
 * case nebitan.
 * 
 * @author brijest
 *
 */
public final class Caseless {
	private String inner;
	
	public Caseless() {
		inner = "";
	}
	
	/**
	 * 
	 * @param val
	 * Ako se preda null, Caseless
	 * ce sadrzavati string "".
	 */
	public Caseless(String val) {
		if (val == null) inner = "";
		else {
			this.inner = val.toLowerCase();
		}
	}
	
	/**
	 * 
	 * @param other
	 * Ako se preda null, Caseless
	 * ce sadrzavati string "".
	 */
	public Caseless(Caseless other) {
		if (other == null) inner = "";
		else {
			this.inner = other.inner;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Caseless) {
			Caseless cas = (Caseless)obj;
			if (this.inner.equals(cas.inner)) return true;
		} else if (obj instanceof String) {
			String str = (String)obj;
			return (this.inner.equalsIgnoreCase(str));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return inner.hashCode();
	}

	@Override
	public String toString() {
		return inner;
	}

	
}
