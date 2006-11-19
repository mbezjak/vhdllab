package hr.fer.zemris.vhdllab.applets.schema.components;

public abstract class AbstractSchemaPort {
	public enum PortDirection { IN, OUT }
	public enum PortOrientation { NORTH, SOUTH, WEST, EAST } 
	
	private Ptr<Object> pName;
	private Ptr<Object> pDir;
	private Ptr<Object> pOrientation;
	
	public AbstractSchemaPort() {
		pName = new Ptr<Object>();
		pName.val = new String("Ime porta.");
		pDir = new Ptr<Object>();
		pDir.val = PortDirection.IN;
		pOrientation = new Ptr<Object>();
		pOrientation.val = PortOrientation.WEST;
	}
	
	/**
	 * S ovim dobivas broj linija
	 * ako se radi o vektoru. Ako se ne
	 * radi, uvijek ces dobiti 1.
	 *
	 */
	public int getPortSize() {
		return 1;
	}
	
	/**
	 * Nema efekta ako radis s
	 * obicnim portom.
	 *
	 */
	public void setPortSize(int ps) {
	}

	/**
	 * @return Returns the direction.
	 */
	public PortDirection getDirection() {
		return (PortDirection) pDir.val;
	}

	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(PortDirection direction) {
		this.pDir.val = direction;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return (String) pName.val;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.pName.val = name;
	}

	/**
	 * @return Returns the orientation.
	 */
	public PortOrientation getOrientation() {
		return (PortOrientation) pOrientation.val;
	}

	/**
	 * @param orientation The orientation to set.
	 */
	public void setOrientation(PortOrientation orientation) {
		this.pOrientation.val = orientation;
	}
	
	// dalje nemoj gledat, nije bitno
	public Ptr<Object> getNamePtr() {
		return pName;
	}
	
	public Ptr<Object> getDirectionPtr() {
		return pDir;
	}
	
	public Ptr<Object> getOrientationPtr() {
		return pOrientation;
	}
}





