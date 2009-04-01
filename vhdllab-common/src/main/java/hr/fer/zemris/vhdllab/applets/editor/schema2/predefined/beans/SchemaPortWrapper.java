package hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans;




/**
 * Port neke komponente.
 * 
 * @author Axel
 *
 */
public final class SchemaPortWrapper {
	
	public static final int NO_PORT = Integer.MIN_VALUE;
	
	private int x, y;
	private String name;
	private String mappedto;
	private int portindex;
	
	public SchemaPortWrapper() {
		x = y = 0;
		name = null;
		mappedto = null;
		portindex = NO_PORT;
	}
	
	public final void setXOffset(Integer x) {
		this.x = x;
	}
	
	public final void setYOffset(Integer y) {
		this.y = y;
	}
	
	public final int getXOffset() {
		return x;
	}
	
	public final int getYOffset() {
		return y;
	}
	
	public final void setXOffset(String xStr) {
		x = Integer.parseInt(xStr);
	}
	
	public final void setYOffset(String yStr) {
		y = Integer.parseInt(yStr);
	}

	/**
	 * Dobavlja ime porta.
	 * 
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Postavlja ime porta.
	 * 
	 * @param name
	 */
	public final void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Vraca zicu na koju je spojen port.
	 * 
	 * @return
	 * Null ako nije spojen ni na sto,
	 * ime signala inace.
	 */
	public final String getMapping() {
		return mappedto;
	}
	
	/**
	 * Spaja port na signal (zicu) danog imena.
	 * 
	 * @param signalToMapTo
	 * Ime signala (zice). Null odspaja
	 * port od signala ako je na njega
	 * bio spojen.
	 */
	public final void setMapping(String signalToMapTo) {
		mappedto = signalToMapTo;
	}
	
	/**
	 * Spaja port na signal (zicu) danog imena.
	 * 
	 * @param signalToMapTo
	 * Ime signala (zice). Null, string od samih praznina
	 * ili prazan string odspajaju.
	 */
	public final void setStringMapping(String signalToMapTo) {
		if (signalToMapTo.trim().equals("")) this.mappedto = null;
		else this.mappedto = signalToMapTo;
	}
	
	public final void setStringName(String name) {
		if (name == null) this.name = "";
		else this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object arg0) {
		if (arg0 == null) return false;
		if (!(arg0 instanceof SchemaPortWrapper)) return false;
		SchemaPortWrapper port = (SchemaPortWrapper)arg0;
		return (x == port.x && y == port.y && port.name.equals(this.name));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return x << 24 + y << 12 + name.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return super.toString();
	}

	public final void setPortindex(int portindex) {
		this.portindex = portindex;
	}

	public final int getPortindex() {
		return portindex;
	}
	
	
	
	
	
}














