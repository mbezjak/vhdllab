package hr.fer.zemris.vhdllab.applets.schema2.misc;




// TODO rijesiti problem razmaka i posebnih znakova

/**
 * U principu String za koji je case nebitan.
 * Pozivom toString() dobiva se originalni string na temelju
 * kojeg je Caseless stvoren - case je ocuvan. Usporedba pomocu
 * equals(), s druge strane, ignorira case.
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
			this.inner = val;
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

	/**
	 * Moguce je obaviti equals provjeru
	 * i sa stringom, ne iskljucivo s drugim
	 * Caselessom. U oba slucaja se casing
	 * ignorira.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Caseless) {
			Caseless cas = (Caseless)obj;
			if (this.inner.equalsIgnoreCase(cas.inner)) return true;
		} else if (obj instanceof String) {
			String str = (String)obj;
			return (this.inner.equalsIgnoreCase(str));
		}
		return false;
	}

	@Override
	public int hashCode() {
//		int hash = 0;
//		char ch;
//		for (int i = 0; i < inner.length(); i++) {
//			ch = inner.charAt(i);
//			if (ch >= 'a' && ch <= 'z') ch -= 32;
//			hash = hash * 41 + (int)(ch);
//		}
//		return hash;
		
		// neefikasnije, ali sigurnije
		return inner.toLowerCase().hashCode();
	}

	@Override
	public String toString() {
		return inner;
	}

	
}
