package hr.fer.zemris.vhdllab.applets.schema2.misc;



/**
 * Port neke komponente.
 * 
 * @author Axel
 *
 */
public final class SchemaPort {
	
	private XYLocation loc;
	private String name;
	
	public SchemaPort() {
		loc = new XYLocation();
		name = "";
	}
	
	public SchemaPort(int xoffset, int yoffset) {
		loc = new XYLocation(xoffset, yoffset);
		name = "";
	}
	
	public SchemaPort(int xoffset, int yoffset, String portname) {
		loc = new XYLocation(xoffset, yoffset);
		name = portname;
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
	 */
	public final void setOffset(XYLocation offset) {
		loc = offset;
	}

	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
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




