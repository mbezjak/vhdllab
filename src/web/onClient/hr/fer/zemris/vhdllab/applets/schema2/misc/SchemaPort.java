package hr.fer.zemris.vhdllab.applets.schema2.misc;



/**
 * Port neke komponente.
 * 
 * @author Axel
 *
 */
public final class SchemaPort {
	
	private XYLocation loc;
	private Caseless name;
	private Caseless mappedto;
	
	public SchemaPort() {
		loc = new XYLocation();
		name = new Caseless("");
		mappedto = null;
	}
	
	public SchemaPort(int xoffset, int yoffset) {
		loc = new XYLocation(xoffset, yoffset);
		name = new Caseless("");
		mappedto = null;
	}
	
	public SchemaPort(int xoffset, int yoffset, Caseless portname) {
		loc = new XYLocation(xoffset, yoffset);
		name = portname;
		mappedto = null;
	}
	
	/**
	 * Vraca offset porta od gornjeg
	 * lijevog kuta komponente.
	 * 
	 * @return
	 */
	public final XYLocation getOffset() {
		return loc;
	}
	
	
	/**
	 * Postavlja offset porta.
	 * 
	 * @param offset
	 * Ako je null, bit ce ocuvana stara vrijednost.
	 */
	public final void setOffset(XYLocation offset) {
		if (offset != null) loc = offset;
	}
	
	public final void setXOffset(int x) {
		loc.x = x;
	}
	
	public final void setYOffset(int y) {
		loc.y = y;
	}

	/**
	 * Dobavlja ime porta.
	 * 
	 * @return
	 */
	public final Caseless getName() {
		return name;
	}

	/**
	 * Postavlja ime porta.
	 * 
	 * @param name
	 */
	public final void setName(Caseless name) {
		this.name = name;
	}
	
	/**
	 * Vraca zicu na koju je spojen port.
	 * 
	 * @return
	 * Null ako nije spojen ni na sto,
	 * ime signala inace.
	 */
	public final Caseless getMapping() {
		return mappedto;
	}
	
	/**
	 * Spaja port na signal (zicu)
	 * danog imena.
	 * 
	 * @param signalToMapTo
	 * Ime signala (zice). Null odspaja
	 * port od signala ako je na njega
	 * bio spojen.
	 */
	public final void setMapping(Caseless signalToMapTo) {
		mappedto = signalToMapTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object arg0) {
		if (arg0 == null) return false;
		if (!(arg0 instanceof SchemaPort)) return false;
		SchemaPort port = (SchemaPort)arg0;
		return (port.loc.equals(this.loc) && port.name.equals(this.name));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return 119 * loc.hashCode() + name.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return super.toString();
	}
	
	
	
	
	
}




